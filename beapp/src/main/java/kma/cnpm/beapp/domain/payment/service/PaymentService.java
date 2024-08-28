package kma.cnpm.beapp.domain.payment.service;

import kma.cnpm.beapp.domain.common.dto.UserDTO;
import kma.cnpm.beapp.domain.common.enumType.TransactionStatus;
import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.payment.dto.request.DepositRequest;
import kma.cnpm.beapp.domain.payment.dto.request.InitDepositRequest;
import kma.cnpm.beapp.domain.payment.dto.response.DepositResponse;
import kma.cnpm.beapp.domain.payment.dto.response.InitDepositResponse;
import kma.cnpm.beapp.domain.payment.entity.Account;
import kma.cnpm.beapp.domain.payment.entity.PaymentGateway;
import kma.cnpm.beapp.domain.payment.entity.Transaction;
import kma.cnpm.beapp.domain.payment.entity.TransactionHistory;
import kma.cnpm.beapp.domain.payment.repository.AccountRepository;
import kma.cnpm.beapp.domain.payment.repository.TransactionRepository;
import kma.cnpm.beapp.domain.user.service.AuthService;
import kma.cnpm.beapp.domain.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentService {
    final AuthService authService;
    final UserService userService;
    final AccountRepository accountRepository;
    final TransactionRepository transactionRepository;
    final  VnpayService vnpayService;

    public DepositResponse deposit(DepositRequest depositRequest) {
        String email = authService.getAuthenticationName();
        UserDTO userDTO = userService.getUserInfo(email);

        Account account = accountRepository.findAccountByUserId(userDTO.getUserId()).orElseThrow(()->new AppException(AppErrorCode.ACCOUNT_NOT_EXIST));
        PaymentGateway paymentGateway = accountRepository.findPaymentGatewayById(userDTO.getUserId()).orElseThrow(()->new AppException(AppErrorCode.PAYMENT_GATEWAY_NOT_EXIST));

        TransactionHistory transactionHistory = TransactionHistory.builder()
                .status(TransactionStatus.PENDING)
                .build();

        Transaction transaction = Transaction.builder()
                .amount(BigDecimal.valueOf(depositRequest.getAmount()))
                .ipAddress(depositRequest.getIpAddress())
                .account(account)
                .paymentGateway(paymentGateway)
                .build();
        transaction.addStatus(transactionHistory);

        Transaction transactionSaved =  transactionRepository.save(transaction);

        InitDepositRequest initDepositRequest = InitDepositRequest.builder()
                .amount(depositRequest.getAmount())
                .txnRef(transactionSaved.getId())
                .requestId(depositRequest.getRequestId())
                .ipAddress(depositRequest.getIpAddress())
                .accountId(account.getId())
                .build();

        InitDepositResponse response =  vnpayService.init(initDepositRequest);
        return DepositResponse.builder()
                .vnpUrl(response.getVnpUrl())
                .idTransaction(initDepositRequest.getTxnRef()).build();
    }
}
