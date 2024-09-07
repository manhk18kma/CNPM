package kma.cnpm.beapp.domain.payment.service;

import kma.cnpm.beapp.domain.common.enumType.WithdrawalStatus;
import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.payment.dto.request.CreateWithdrawalRequest;
import kma.cnpm.beapp.domain.payment.dto.response.AccountResponse;
import kma.cnpm.beapp.domain.payment.entity.Account;
import kma.cnpm.beapp.domain.payment.entity.AccountHasBank;
import kma.cnpm.beapp.domain.payment.entity.Withdrawal;
import kma.cnpm.beapp.domain.payment.repository.AccountHasBankRepository;
import kma.cnpm.beapp.domain.payment.repository.AccountRepository;
import kma.cnpm.beapp.domain.payment.repository.WithdrawalRepository;
import kma.cnpm.beapp.domain.user.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WithdrawalService {
    final WithdrawalRepository withdrawalRepository;
    final AccountService accountService;
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
            case REJECTED:
                throw new AppException(AppErrorCode.WITHDRAWAL_REJECTED_ERROR);
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
        return AccountResponse.builder()
                .accountId(account.getId())
                .build();
    }

    @Transactional
    public void cancelWithdrawal(Long id) {
        Account account = accountService.getAccount();
        Withdrawal withdrawal = getPendingWithdrawal(id);

        if (!withdrawal.getAccount().getId().equals(account.getId())) {
            throw new AppException(AppErrorCode.UNAUTHORIZED);
        }
        withdrawal.setStatus(WithdrawalStatus.CANCELLED);
        withdrawalRepository.save(withdrawal);
    }


    @Transactional
    public void approveWithdrawal(Long id) {
        Account account = accountService.getAccount();
        Withdrawal withdrawal = getPendingWithdrawal(id);
        withdrawal.setStatus(WithdrawalStatus.APPROVED);
        accountService.updateBalance(withdrawal.getAmount() , account , false);
    }

}
