package kma.cnpm.beapp.domain.payment.service;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import kma.cnpm.beapp.domain.common.enumType.Currency;
import kma.cnpm.beapp.domain.common.enumType.TransactionStatus;
import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

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

//    Small function
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
                .idTransaction(initDepositRequest.getTxnRef()).build();
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
        accountService.updateBalance(amount , account , true);

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
                .idTransaction(transactionSaved.getId())
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
            accountService.updateBalance(transaction.getAmount() ,account , true );
        }
    }

    public VnpayTransactionResponse getVnpayTransactionInfo(Long id) {
        return null;
    }
}
