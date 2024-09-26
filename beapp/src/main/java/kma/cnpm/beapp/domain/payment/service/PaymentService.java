package kma.cnpm.beapp.domain.payment.service;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import kma.cnpm.beapp.domain.common.dto.PageResponse;
import kma.cnpm.beapp.domain.common.enumType.*;
import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.common.validation.EnumValue;
import kma.cnpm.beapp.domain.payment.dto.request.DepositPaypalRequest;
import kma.cnpm.beapp.domain.payment.dto.request.DepositVnPayRequest;
import kma.cnpm.beapp.domain.payment.dto.request.InitDepositVnPayRequest;
import kma.cnpm.beapp.domain.payment.dto.response.*;
import kma.cnpm.beapp.domain.payment.entity.*;
import kma.cnpm.beapp.domain.payment.repository.AccountRepository;
import kma.cnpm.beapp.domain.payment.repository.TransactionRepository;
import kma.cnpm.beapp.domain.payment.service.FetchRating.ExchangeRate;
import kma.cnpm.beapp.domain.payment.service.FetchRating.ExchangeRateService;
import kma.cnpm.beapp.domain.user.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentService {
    final AccountRepository accountRepository;
    final TransactionRepository transactionRepository;
    final  VnpayService vnpayService;
    final ExchangeRateService exchangeRateService;
    final PaypalService paypalService;
    final AccountService accountService;
    @Value("${urlClient}")
    String urlClient;
    final AuthService authService;

//    Helper function
    PaymentGateway getPaymentGateway(Long id){
        return accountRepository.findPaymentGatewayById(id).orElseThrow(()->new AppException(AppErrorCode.PAYMENT_GATEWAY_NOT_EXIST));
    }
    TransactionHistory initTransactionHistory(TransactionStatus status){
        return TransactionHistory.builder()
                .status(status)
                .build();
    }
    Transaction getTransaction(Long id){
        return transactionRepository.findById(id)
                .orElseThrow(() -> new AppException(AppErrorCode.TRANSACTION_NOT_EXISTED));
    }

//Main functions
    @Transactional
    public DepositResponseVnPay depositVnPay(DepositVnPayRequest depositRequest, String ipAddress) {
        Account account = accountService.getAccount();
        PaymentGateway paymentGateway = getPaymentGateway(depositRequest.getPaymentGatewayId());

        TransactionHistory transactionHistory = initTransactionHistory(TransactionStatus.PENDING);

        Transaction transaction = Transaction.builder()
                .amount(BigDecimal.valueOf(depositRequest.getAmount()))
                .ipAddress(ipAddress)
                .account(account)
                .status(TransactionStatus.PENDING)
                .paymentGateway(paymentGateway)
                .currency(Currency.VND)
                .build();

        transaction.addStatus(transactionHistory);
        account.addTransaction(transaction);

        Transaction transactionSaved =  transactionRepository.save(transaction);

        InitDepositVnPayRequest initDepositRequest = InitDepositVnPayRequest.builder()
                .amount(depositRequest.getAmount())
                .txnRef(transactionSaved.getId())
                .ipAddress(ipAddress)
                .accountId(account.getId())
                .build();

        InitDepositResponse response =  vnpayService.init(initDepositRequest);
        return DepositResponseVnPay.builder()
                .vnpUrl(response.getVnpUrl())
                .userId(account.getUserId())
                .accountId(account.getId())
                .transactionId(initDepositRequest.getTxnRef()).build();
    }

    public void handleCallbackVNPay(Map<String, String> params) {
        VnpayResponse vnpayResponse = new VnpayResponse(params);

        // Xác thực và tìm giao dịch
        Long transactionId = Long.valueOf(vnpayResponse.getVnpTxnRef());
        Transaction transaction = getTransaction(transactionId);

        // Cập nhật trạng thái giao dịch
        TransactionHistory transactionHistory = initTransactionHistory(TransactionStatus.COMPLETED);
        transaction.addStatus(transactionHistory);
        transaction.setStatus(TransactionStatus.COMPLETED);

        // Cập nhật số dư tài khoản
        BigDecimal amount = new BigDecimal(vnpayResponse.getVnpAmount()).divide(BigDecimal.valueOf(100));
        Account account = transaction.getAccount();
        accountService.updateBalance(amount , account , true,transaction.getId() , NotificationType.BALANCE_CHANGE);

    }
    public DepositResponsePaypal depositPaypal(DepositPaypalRequest request, String ipAddress) throws PayPalRESTException {
        Account account = accountService.getAccount();
        PaymentGateway paymentGateway = getPaymentGateway(request.getPaymentGatewayId());

        ExchangeRate exchangeRate = exchangeRateService.getExchangeRates();
        if (exchangeRate == null || exchangeRate.getUsdToVnd() == null) {
            throw new IllegalStateException("Exchange rate or USD to VND rate is not available");
        }
//Init payment with paypal
        BigDecimal amountInUsd = request.getAmount();
        String successUrl = urlClient+"/payment/success";
        String cancelUrl =  urlClient+"/payment/cancel";
        Payment payment = paypalService.createPayment(amountInUsd, cancelUrl, successUrl);


        PaypalTransaction transaction = new PaypalTransaction();
//        transaction.setAmount(amountInVnd);
        transaction.setIpAddress(ipAddress);
        transaction.setAccount(account);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setPaymentGateway(paymentGateway);
        transaction.setPaymentId(payment.getId());
        transaction.setPayerId(null);
        transaction.setCurrency(Currency.USD);
        transaction.setUsdToVnd(exchangeRate.getUsdToVnd());
        transaction.setUsdToEur(exchangeRate.getUsdToEur());
        transaction.setAmountInUsd(amountInUsd);

        TransactionHistory transactionHistory = initTransactionHistory(TransactionStatus.PENDING);

        transaction.addStatus(transactionHistory);
        account.addTransactionPaypal(transaction);

        PaypalTransaction transactionSaved = transactionRepository.save(transaction);

        String paypalUrl = payment.getLinks().stream()
                .filter(link -> "approval_url".equals(link.getRel()))
                .map(Links::getHref)
                .findFirst()
                .orElse("");

        if (paypalUrl.isEmpty()) {
            throw new IllegalStateException("PayPal approval URL is not available");
        }

        // Trả về đối tượng DepositResponsePaypal với thông tin giao dịch và URL thanh toán
        return DepositResponsePaypal.builder()
                .userId(account.getUserId())
                .accountId(account.getId())
                .transactionId(transactionSaved.getId())
                .paypalUrl(paypalUrl)
                .cancelUrl(cancelUrl)
                .successUrl(successUrl)
                .build();
    }


    public void handleCallbackPaypal(String payload) throws PayPalRESTException {
        PaypalResponse paypalResponse = new PaypalResponse(payload);

        PaypalTransaction transaction = transactionRepository.findTransactionByPaymentId(paypalResponse.getPaymentId())
                .orElseThrow(() -> new AppException(AppErrorCode.TRANSACTION_NOT_EXISTED));

        // Xử lý event "PAYMENTS.PAYMENT.CREATED"
        if (paypalResponse.getEventType().equals("PAYMENTS.PAYMENT.CREATED")) {
            BigDecimal usdToVnd = transaction.getUsdToVnd();  // Tỷ giá USD/VND
            BigDecimal amountInUsd = paypalResponse.getTotal(); // Tổng số tiền trong USD từ PayPal
            BigDecimal amountInVnd = amountInUsd.multiply(usdToVnd);  // Chuyển đổi số tiền sang VND
            transaction.setAmount(amountInVnd);  // Cập nhật số tiền trong giao dịch

            TransactionHistory transactionHistory = initTransactionHistory(TransactionStatus.PROCESSED);
            transaction.addStatus(transactionHistory);
            transaction.setStatus(TransactionStatus.PROCESSED);

            transactionRepository.save(transaction);
            paypalService.executePayment(paypalResponse.getPaymentId(),paypalResponse.getPayerId());

        } else if (paypalResponse.getEventType().equals("PAYMENT.SALE.COMPLETED")) {  // Xử lý event "PAYMENT.SALE.COMPLETED"
            TransactionHistory transactionHistory = initTransactionHistory(TransactionStatus.COMPLETED);
            transaction.addStatus(transactionHistory);
            transaction.setStatus(TransactionStatus.COMPLETED);

            // Cập nhật số dư tài khoản
            Account account = transaction.getAccount();
            accountService.updateBalance(transaction.getAmount() ,account , true , transaction.getId() , NotificationType.BALANCE_CHANGE);
        }
    }
//    public PageResponse<List<TransactionResponse>> getVnpayTransactionInfoOfUser(String status, String sortBy, int pageNo, int pageSize) {
//        Long userId = Long.valueOf(authService.getAuthenticationName());
//
//        TransactionStatus transactionStatus =  TransactionStatus.valueOf(status);
//        WithdrawalSort withdrawalSort =  WithdrawalSort.valueOf(sortBy);
////
//        String fieldSort = withdrawalSort.getField();
//        String orderBy = withdrawalSort.getDirection();
//
//        // Tạo đối tượng Pageable với sắp xếp và phân trang
//        Sort sort = Sort.by(Sort.Order.by(fieldSort).with(Sort.Direction.fromString(orderBy)));
//        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
//
//        Page<Transaction> transactionPage = transactionRepository.getVnpayTransactionInfoOfUser(userId, transactionStatus == TransactionStatus.DEFAULT ? null : transactionStatus, pageable);
//
//        List<TransactionResponse> responses = transactionPage.getContent()
//                .stream()
//                .map(transaction -> {
//                    Set<TransactionHistory> transactionHistories = transaction.getTransactionHistories();
//                    List<TransactionResponse.TransactionHistoryResponse> transactionHistoryResponses =
//                            transactionHistories.stream().map(transactionHistory -> {
//                                return TransactionResponse.TransactionHistoryResponse.builder()
//                                        .timestamp(transactionHistory.getCreatedAt())
//                                        .status(transactionHistory.getStatus())
//                                        .build();
//                            }).collect(Collectors.toList());
//
//                    TransactionResponse.PaymentGatewayResponse paymentGatewayResponse = TransactionResponse.PaymentGatewayResponse.builder()
//                            .name(transaction.getPaymentGateway().getName())
//                            .paymentGatewayAvt(transaction.getPaymentGateway().getPaymentGatewayAvt())
//                            .build();
//
//
//                    return TransactionResponse.builder()
//                            .transactionId(transaction.getId())
//                            .amount(transaction.getAmount())
//                            .userId(userId)
//                            .status(transaction.getStatus())
//                            .ipAddress(transaction.getIpAddress())
//                            .currencyPay(transaction.getCurrency())
//                            .createAt(transaction.getCreatedAt())
//                            .transactionHistories(transactionHistoryResponses)
//                            .paymentGateway(paymentGatewayResponse)
//                            .build();
//                }).collect(Collectors.toList());
//
//        return PageResponse.<List<TransactionResponse>>builder()
//                .pageSize(pageSize)
//                .totalElements((int) transactionPage.getTotalElements())
//                .totalPages(transactionPage.getTotalPages())
//                .pageNo(pageNo)
//                .items(responses)
//                .build();
//    }

//    public PageResponse<List<TransactionResponse>> getPaypalTransactionInfoOfUser(String status, String sortBy, int pageNo, int pageSize) {
//        Long userId = Long.valueOf(authService.getAuthenticationName());
//
//        TransactionStatus transactionStatus =  TransactionStatus.valueOf(status);
//        WithdrawalSort withdrawalSort =  WithdrawalSort.valueOf(sortBy);
//        String fieldSort = withdrawalSort.getField();
//        String orderBy = withdrawalSort.getDirection();
//
//        // Tạo đối tượng Pageable với sắp xếp và phân trang
//        Sort sort = Sort.by(Sort.Order.by(fieldSort).with(Sort.Direction.fromString(orderBy)));
//        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
//
//        Page<PaypalTransaction> transactionPage = transactionRepository.getPaypalTransactionInfoOfUser(userId, transactionStatus == TransactionStatus.DEFAULT ? null : transactionStatus, pageable);
//
//        List<TransactionResponse> responses = transactionPage.getContent()
//                .stream()
//                .map(transaction -> {
//                    Set<TransactionHistory> transactionHistories = transaction.getTransactionHistories();
//                    List<TransactionResponse.TransactionHistoryResponse> transactionHistoryResponses =
//                            transactionHistories.stream().map(transactionHistory -> {
//                                return TransactionResponse.TransactionHistoryResponse.builder()
//                                        .timestamp(transactionHistory.getCreatedAt())
//                                        .status(transactionHistory.getStatus())
//                                        .build();
//                            }).collect(Collectors.toList());
//
//                    TransactionResponse.PaymentGatewayResponse paymentGatewayResponse = TransactionResponse.PaymentGatewayResponse.builder()
//                            .name(transaction.getPaymentGateway().getName())
//                            .paymentGatewayAvt(transaction.getPaymentGateway().getPaymentGatewayAvt())
//                            .build();
//
//
//                    return TransactionResponse.builder()
//                            .transactionId(transaction.getId())
//                            .amount(transaction.getAmount())
//                            .userId(userId)
//                            .status(transaction.getStatus())
//                            .ipAddress(transaction.getIpAddress())
//                            .currencyPay(transaction.getCurrency())
//                            .createAt(transaction.getCreatedAt())
//                            .transactionHistories(transactionHistoryResponses)
//                            .paymentGateway(paymentGatewayResponse)
//                            .usdToVnd(transaction.getUsdToVnd())
//                            .amountInUsd(transaction.getAmountInUsd())
//                            .build();
//                }).collect(Collectors.toList());
//
//        return PageResponse.<List<TransactionResponse>>builder()
//                .pageSize(pageSize)
//                .totalElements((int) transactionPage.getTotalElements())
//                .totalPages(transactionPage.getTotalPages())
//                .pageNo(pageNo)
//                .items(responses)
//                .build();
//    }

    public PageResponse<List<TransactionResponse>> getTransactionInfoOfUser(String status, String sortBy, String type, int pageNo, int pageSize) {
        Long userId = Long.valueOf(authService.getAuthenticationName());
//        Status
        TransactionStatus transactionStatus =  TransactionStatus.valueOf(status);
//        SortBy
        WithdrawalSort withdrawalSort =  WithdrawalSort.valueOf(sortBy);
        String fieldSort = withdrawalSort.getField();
        String orderBy = withdrawalSort.getDirection();
//        TransactionType
        TransactionType transactionType = TransactionType.valueOf(type);
//Pagination
        Sort sort = Sort.by(Sort.Order.by(fieldSort).with(Sort.Direction.fromString(orderBy)));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        System.out.println(transactionType);

        Page<? extends Transaction> transactionPage = switch (transactionType) {
            case VNPAY_TRANSACTION -> transactionRepository.getVnpayTransactionInfoOfUser(
                    userId,
                    transactionStatus == TransactionStatus.DEFAULT ? null : transactionStatus,
                    pageable);
            case PAYPAL_TRANSACTION -> transactionRepository.getPaypalTransactionInfoOfUser(
                    userId,
                    transactionStatus == TransactionStatus.DEFAULT ? null : transactionStatus,
                    pageable);
            default -> transactionRepository.getTransactionInfoOfUser(
                    userId,
                    transactionStatus == TransactionStatus.DEFAULT ? null : transactionStatus,
                    pageable);
        };


        List<TransactionResponse> responses = transactionPage.getContent()
                .stream()
                .map(transaction -> {
//                    build history
                    Set<TransactionHistory> transactionHistories = transaction.getTransactionHistories();
                    List<TransactionResponse.TransactionHistoryResponse> transactionHistoryResponses =
                            transactionHistories.stream().map(transactionHistory -> {
                                return TransactionResponse.TransactionHistoryResponse.builder()
                                        .timestamp(transactionHistory.getCreatedAt())
                                        .status(transactionHistory.getStatus())
                                        .build();
                            }).collect(Collectors.toList());
//                    build payment gateway
                    TransactionResponse.PaymentGatewayResponse paymentGatewayResponse = TransactionResponse.PaymentGatewayResponse.builder()
                            .name(transaction.getPaymentGateway().getName())
                            .paymentGatewayAvt(transaction.getPaymentGateway().getPaymentGatewayAvt())
                            .build();

 //                    build exchange rate if it is paypal

                    BigDecimal usdToVnd = null;
                    BigDecimal amountInUsd = null;
                    if (transaction instanceof PaypalTransaction) {
                        usdToVnd = ((PaypalTransaction) transaction).getUsdToVnd();
                        amountInUsd = ((PaypalTransaction) transaction).getAmountInUsd();
                    }

                    return TransactionResponse.builder()
                            .transactionId(transaction.getId())
                            .amount(transaction.getAmount())
                            .userId(userId)
                            .accountId(transaction.getAccount().getId())
                            .status(transaction.getStatus())
                            .ipAddress(transaction.getIpAddress())
                            .currencyPay(transaction.getCurrency())
                            .createAt(transaction.getCreatedAt())
                            .transactionHistories(transactionHistoryResponses)
                            .paymentGateway(paymentGatewayResponse)
                            .usdToVnd(usdToVnd)
                            .amountInUsd(amountInUsd)
                            .build();
                }).collect(Collectors.toList());

        return PageResponse.<List<TransactionResponse>>builder()
                .pageSize(pageSize)
                .totalElements((int) transactionPage.getTotalElements())
                .totalPages(transactionPage.getTotalPages())
                .pageNo(pageNo)
                .items(responses)
                .build();

    }
}
