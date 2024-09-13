package kma.cnpm.beapp.domain.payment.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import kma.cnpm.beapp.domain.payment.dto.response.PaypalResponse;
import kma.cnpm.beapp.domain.payment.repository.AccountRepository;
import kma.cnpm.beapp.domain.payment.repository.PaypalTransactionRepository;
import kma.cnpm.beapp.domain.payment.service.FetchRating.ExchangeRateService;
import kma.cnpm.beapp.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PaypalService {

    private final APIContext apiContext; // Đối tượng APIContext dùng để kết nối với PayPal API

//    public Payment createPayment(
//            Double total,
//            String currency,
//            String method,
//            String intent,
//            String description,
//            String cancelUrl,
//            String successUrl
//    ) throws PayPalRESTException {
//        System.out.println("Creating payment...");
//
//        // Tạo đối tượng Amount chứa thông tin về số tiền và loại tiền tệ
//        Amount amount = new Amount();
//        amount.setCurrency(currency);
//        amount.setTotal(String.format(Locale.forLanguageTag(currency), "%.2f", total)); // Định dạng số tiền
//
//        // Tạo đối tượng Transaction chứa thông tin về giao dịch
//        Transaction transaction = new Transaction();
//        transaction.setDescription(description);
//        transaction.setAmount(amount);
//
//        // Tạo danh sách các giao dịch và thêm giao dịch vừa tạo vào danh sách
//        List<Transaction> transactions = new ArrayList<>();
//        transactions.add(transaction);
//
//        // Tạo đối tượng Payer để chỉ định phương thức thanh toán
//        Payer payer = new Payer();
//        payer.setPaymentMethod(method);
//
//        // Tạo đối tượng Payment và thiết lập các thông tin cần thiết
//        Payment payment = new Payment();
//        payment.setIntent(intent);
//        payment.setPayer(payer);
//        payment.setTransactions(transactions);
//
//        // Tạo đối tượng RedirectUrls chứa URL để chuyển hướng người dùng
//        RedirectUrls redirectUrls = new RedirectUrls();
//        redirectUrls.setCancelUrl(cancelUrl);
//        redirectUrls.setReturnUrl(successUrl);
//        payment.setRedirectUrls(redirectUrls);
//
//        // Gọi PayPal API để tạo thanh toán và nhận phản hồi
//        Payment createdPayment = payment.create(apiContext);
//
//
//        return createdPayment;
//    }

    public Payment createPayment(BigDecimal total, String cancelUrl, String successUrl) throws PayPalRESTException {
        System.out.println("Creating payment...");

        // Tạo đối tượng Amount chứa thông tin về số tiền và loại tiền tệ
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(String.format(Locale.forLanguageTag("USD"), "%.2f", total));

        // Tạo đối tượng Transaction và thiết lập các thông tin cần thiết
        Transaction transaction = new Transaction();
        transaction.setDescription("Description");
        transaction.setAmount(amount);

        // Tạo danh sách các giao dịch và thêm giao dịch vừa tạo vào danh sách
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        // Tạo đối tượng Payer để chỉ định phương thức thanh toán
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        // Tạo đối tượng Payment và thiết lập các thông tin cần thiết
        Payment payment = new Payment();
        payment.setIntent("sale"); // Sửa từ "funds" thành "sale" để phù hợp với mục đích thanh toán
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        // Tạo đối tượng RedirectUrls chứa URL để chuyển hướng người dùng
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        // Gọi PayPal API để tạo thanh toán và nhận phản hồi
        Payment createdPayment = payment.create(apiContext);

        return createdPayment;
    }

    public Payment executePayment(
            String paymentId,
            String payerId
    ) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecution);
    }



}
