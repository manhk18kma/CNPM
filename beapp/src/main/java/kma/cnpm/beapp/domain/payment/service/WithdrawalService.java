package kma.cnpm.beapp.domain.payment.service;

import kma.cnpm.beapp.domain.common.dto.PageResponse;
import kma.cnpm.beapp.domain.common.enumType.NotificationType;
import kma.cnpm.beapp.domain.common.enumType.WithdrawalSort;
import kma.cnpm.beapp.domain.common.enumType.WithdrawalStatus;
import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.common.notificationDto.WithdrawalCreated;
import kma.cnpm.beapp.domain.common.notificationDto.WithdrawalRejected;
import kma.cnpm.beapp.domain.notification.service.NotificationService;
import kma.cnpm.beapp.domain.payment.dto.request.CreateWithdrawalRequest;
import kma.cnpm.beapp.domain.payment.dto.response.AccountResponse;
import kma.cnpm.beapp.domain.payment.dto.response.WithdrawalResponse;
import kma.cnpm.beapp.domain.payment.entity.Account;
import kma.cnpm.beapp.domain.payment.entity.AccountHasBank;
import kma.cnpm.beapp.domain.payment.entity.Withdrawal;
import kma.cnpm.beapp.domain.payment.repository.WithdrawalRepository;
import kma.cnpm.beapp.domain.user.dto.response.SearchUserResponse;
import kma.cnpm.beapp.domain.user.service.AuthService;
import kma.cnpm.beapp.domain.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WithdrawalService {
    final WithdrawalRepository withdrawalRepository;
    final AccountService accountService;
    final AuthService authService;
    final UserService userService;
    final NotificationService notificationService;
//small function
    void existsPendingWithdrawal(Long accountId){
        if(withdrawalRepository.existsPendingWithdrawal(accountId)){
            throw new AppException(AppErrorCode.PENDING_WITHDRAWAL_ALREADY_EXISTS);
        }
    }
    public Withdrawal getPendingWithdrawal(Long id) {
        Withdrawal withdrawal = withdrawalRepository.findWithdrawalById(id)
                .orElseThrow(() -> new AppException(AppErrorCode.WITHDRAWAL_NOT_EXIST));

        WithdrawalStatus withdrawalStatus = withdrawal.getStatus();
        switch (withdrawalStatus) {
            case PENDING:
                return withdrawal;
            case APPROVED:
                throw new AppException(AppErrorCode.WITHDRAWAL_APPROVED_ERROR);
            case CANCELLED:
                throw new AppException(AppErrorCode.WITHDRAWAL_CANCELLED_ERROR);
            default:
                throw new AppException(AppErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }


//main function
    @Transactional
    public AccountResponse createWithdrawal(CreateWithdrawalRequest request) {
        Account account = accountService.getAccount();
        existsPendingWithdrawal(account.getId());
        AccountHasBank accountHasBank = accountService.getAccountHasBank(request.getAccountHasBankId());
        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new AppException(AppErrorCode.BALANCE_NOT_ENOUGH);
        }
        Withdrawal withdrawal = Withdrawal.builder()
                .amount(request.getAmount())
                .status(WithdrawalStatus.PENDING)
                .build();
        account.addWithdrawals(withdrawal);
        accountHasBank.addWithdrawals(withdrawal);
        withdrawalRepository.save(withdrawal);

        //send notification
        notificationService.withdrawalCreated(WithdrawalCreated.builder()
                        .withdrawalId(withdrawal.getId())
                        .amount(request.getAmount())
                .build());
        return AccountResponse.builder()
                .accountId(account.getId())
                .userId(account.getUserId())
                .withdrawalId(withdrawal.getId())
                .build();
    }

    @Transactional
    public AccountResponse cancelWithdrawal(Long id) {
        Account account = accountService.getAccount();
        Withdrawal withdrawal = getPendingWithdrawal(id);

        if (!withdrawal.getAccount().getId().equals(account.getId())) {
            throw new AppException(AppErrorCode.UNAUTHORIZED);
        }
        withdrawal.setStatus(WithdrawalStatus.CANCELLED);
        withdrawalRepository.save(withdrawal);
        return AccountResponse.builder()
                .accountId(account.getId())
                .userId(account.getUserId())
                .withdrawalId(withdrawal.getId())
                .build();
    }


    @Transactional
    public AccountResponse approveWithdrawal(Long id) {
        Withdrawal withdrawal = getPendingWithdrawal(id);
        Account account = withdrawal.getAccount();

        withdrawal.setStatus(WithdrawalStatus.APPROVED);
        accountService.updateBalance(withdrawal.getAmount() , account , false , id , NotificationType.WITHDRAWAL_ACCEPTED);
        return AccountResponse.builder()
                .accountId(account.getId())
                .userId(account.getUserId())
                .withdrawalId(withdrawal.getId())
                .build();
    }
//user get of user
    public PageResponse<List<WithdrawalResponse>> getWithdrawalsOfUser( String status, String sortBy, int page, int size) {
        Long userId = Long.valueOf(authService.getAuthenticationName());

        WithdrawalStatus withdrawalStatus =  WithdrawalStatus.valueOf(status);
        WithdrawalSort withdrawalSort =  WithdrawalSort.valueOf(sortBy);

        String fieldSort = withdrawalSort.getField();
        String orderBy = withdrawalSort.getDirection();

        // Tạo đối tượng Pageable với sắp xếp và phân trang
        Sort sort = Sort.by(Sort.Order.by(fieldSort).with(Sort.Direction.fromString(orderBy)));
        Pageable pageable = PageRequest.of(page, size, sort);

        // Gọi repository để lấy dữ liệu
        Page<Withdrawal> withdrawalsPage = withdrawalRepository.getWithdrawalsOfUser(userId, withdrawalStatus == WithdrawalStatus.DEFAULT ? null : withdrawalStatus, pageable);

        List<WithdrawalResponse> responses = withdrawalsPage.getContent().stream()
                .map(withdrawal -> WithdrawalResponse.builder()
                        .withdrawalId(withdrawal.getId())
                        .userId(userId)
                        .fullName(authService.getUserInfo(withdrawal.getAccount().getUserId()).getFullName())

                        .accountId(withdrawal.getAccount().getId())
                        .amount(withdrawal.getAmount())
                        .status(withdrawal.getStatus())
                        .accountNumber(withdrawal.getAccountHasBank().getAccountNumber())
                        .bankName(withdrawal.getAccountHasBank().getBank().getBankName())
                        .bankAvt(withdrawal.getAccountHasBank().getBank().getBankAvt())
                        .bankCode(withdrawal.getAccountHasBank().getBank().getBankCode())
                        .createdAt(withdrawal.getCreatedAt())
                        .build()
                )
                .collect(Collectors.toList());


        return PageResponse.<List<WithdrawalResponse>>builder()
                .pageSize(size)
                .totalElements((int) withdrawalsPage.getTotalElements())
                .totalPages(withdrawalsPage.getTotalPages())
                .pageNo(page)
                .items(responses)
                .build();


    }



//admin get of all user
    public PageResponse<List<WithdrawalResponse>> getWithdrawalsOfAllUsers(String status, String sortBy, int page, int size) {

        WithdrawalStatus withdrawalStatus =  WithdrawalStatus.valueOf(status);
        WithdrawalSort withdrawalSort =  WithdrawalSort.valueOf(sortBy);

        String fieldSort = withdrawalSort.getField();
        String orderBy = withdrawalSort.getDirection();

        // Tạo đối tượng Pageable với sắp xếp và phân trang
        Sort sort = Sort.by(Sort.Order.by(fieldSort).with(Sort.Direction.fromString(orderBy)));
        Pageable pageable = PageRequest.of(page, size, sort);

        // Gọi repository để lấy dữ liệu
        Page<Withdrawal> withdrawalsPage = withdrawalRepository.getWithdrawalsOfAllUsers(withdrawalStatus == WithdrawalStatus.DEFAULT ? null : withdrawalStatus, pageable);

        List<WithdrawalResponse> responses = withdrawalsPage.getContent().stream()
                .map(withdrawal -> WithdrawalResponse.builder()
                        .withdrawalId(withdrawal.getId())
                        .fullName(authService.getUserInfo(withdrawal.getAccount().getUserId()).getFullName())
                        .userId(withdrawal.getAccount().getUserId())
                        .accountId(withdrawal.getAccount().getId())
                        .amount(withdrawal.getAmount())
                        .status(withdrawal.getStatus())
                        .accountNumber(withdrawal.getAccountHasBank().getAccountNumber())
                        .bankName(withdrawal.getAccountHasBank().getBank().getBankName())
                        .bankAvt(withdrawal.getAccountHasBank().getBank().getBankAvt())
                        .bankCode(withdrawal.getAccountHasBank().getBank().getBankCode())
                        .createdAt(withdrawal.getCreatedAt())
                        .build()
                )
                .collect(Collectors.toList());


        return PageResponse.<List<WithdrawalResponse>>builder()
                .pageSize(size)
                .totalElements((int) withdrawalsPage.getTotalElements())
                .totalPages(withdrawalsPage.getTotalPages())
                .pageNo(page)
                .items(responses)
                .build();

    }

    public AccountResponse rejectWithdrawal(Long id) {
        Withdrawal withdrawal = getPendingWithdrawal(id);
        withdrawal.setStatus(WithdrawalStatus.REJECTED);
        withdrawalRepository.save(withdrawal);
        notificationService.withdrawalRejected(WithdrawalRejected.builder()
                        .withdrawalId(withdrawal.getId())
                        .userId(withdrawal.getAccount().getUserId())
                        .amount(withdrawal.getAmount())

                .build());
        return AccountResponse.builder()
//                .accountId(account.getId())
//                .userId(account.getUserId())
                .withdrawalId(withdrawal.getId())
                .build();
    }
}
