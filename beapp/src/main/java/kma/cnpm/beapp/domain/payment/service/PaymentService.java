package kma.cnpm.beapp.domain.payment.service;

import kma.cnpm.beapp.domain.common.dto.UserDTO;
import kma.cnpm.beapp.domain.common.enumType.TransactionStatus;
import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.payment.dto.request.DepositRequest;
import kma.cnpm.beapp.domain.payment.dto.request.InitDepositRequest;
import kma.cnpm.beapp.domain.payment.dto.response.DepositResponse;
import kma.cnpm.beapp.domain.payment.dto.response.InitDepositResponse;
import kma.cnpm.beapp.domain.payment.dto.response.VnpayResponse;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentService {
    final AuthService authService;
    final AccountRepository accountRepository;
    final TransactionRepository transactionRepository;
    final  VnpayService vnpayService;

    public DepositResponse deposit(DepositRequest depositRequest) {
        String userId = authService.getAuthenticationName();
//        UserDTO userDTO = userService.getUserInfo(userId);

        Account account = accountRepository.findAccountByUserId(Long.valueOf(userId)).orElseThrow(()->new AppException(AppErrorCode.ACCOUNT_NOT_EXIST));
//        PaymentGateway paymentGateway = accountRepository.findPaymentGatewayById(userDTO.getUserId()).orElseThrow(()->new AppException(AppErrorCode.PAYMENT_GATEWAY_NOT_EXIST));

        TransactionHistory transactionHistory = TransactionHistory.builder()
                .status(TransactionStatus.PENDING)
                .build();

        Transaction transaction = Transaction.builder()
                .amount(BigDecimal.valueOf(depositRequest.getAmount()))
                .ipAddress(depositRequest.getIpAddress())
                .account(account)
                .status(TransactionStatus.PENDING)
//                .paymentGateway(paymentGateway)
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

    public void handleCallback(VnpayResponse vnpayResponse) {
        Optional<Transaction> optionalTransaction = transactionRepository.findTranById(Long.valueOf(vnpayResponse.getVnpTxnRef()));
        Transaction transaction = optionalTransaction.orElseThrow(() ->
                new AppException(AppErrorCode.TRANSACTION_NOT_EXISTED)
        );


        TransactionHistory transactionHistory = TransactionHistory.builder().status(TransactionStatus.COMPLETED).build();
        transaction.addStatus(transactionHistory);
//        transaction.setS

        Account account = transaction.getAccount();
        account.setBalance(account.getBalance().add(BigDecimal.valueOf(Long.valueOf(vnpayResponse.getVnpAmount())).divide(BigDecimal.valueOf(100))));
        accountRepository.save(account);
        transactionRepository.save(transaction);
    }
}
