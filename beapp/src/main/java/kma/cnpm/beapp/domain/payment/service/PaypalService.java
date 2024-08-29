package kma.cnpm.beapp.domain.payment.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PaypalService {

    private final APIContext apiContext; // Đối tượng APIContext dùng để kết nối với PayPal API

    /**
     * Tạo một yêu cầu thanh toán mới trên PayPal.
     *
     * @param total Tổng số tiền thanh toán
     * @param currency Loại tiền tệ (ví dụ: USD, EUR)
     * @param method Phương thức thanh toán (ví dụ: "paypal")
     * @param intent Mục đích thanh toán (ví dụ: "sale")
     * @param description Mô tả thanh toán
     * @param cancelUrl URL để chuyển hướng người dùng nếu thanh toán bị hủy
     * @param successUrl URL để chuyển hướng người dùng nếu thanh toán thành công
     * @return Đối tượng Payment chứa thông tin về thanh toán
     * @throws PayPalRESTException Nếu có lỗi khi gọi PayPal API
     */
    public Payment createPayment(
            Double total,
            String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl
    ) throws PayPalRESTException {
        System.out.println("createPaymentcreatePaymentcreatePaymentcreatePaymentcreatePaymentcreatePaymentcreatePaymentcreatePayment");
        // Tạo đối tượng Amount chứa thông tin về số tiền và loại tiền tệ
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format(Locale.forLanguageTag(currency), "%.2f", total)); // Định dạng số tiền

        // Tạo đối tượng Transaction chứa thông tin về giao dịch
        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        // Tạo danh sách các giao dịch và thêm giao dịch vừa tạo vào danh sách
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        // Tạo đối tượng Payer để chỉ định phương thức thanh toán
        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        // Tạo đối tượng Payment và thiết lập các thông tin cần thiết
        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        // Tạo đối tượng RedirectUrls chứa URL để chuyển hướng người dùng
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        // Gọi PayPal API để tạo thanh toán và trả về đối tượng Payment
        return payment.create(apiContext);
    }

    /**
     * Xác thực và thực hiện thanh toán sau khi người dùng đã chấp thuận trên PayPal.
     *
     * @param paymentId ID của thanh toán
     * @param payerId ID của người thanh toán
     * @return Đối tượng Payment chứa thông tin về thanh toán đã được xác thực
     * @throws PayPalRESTException Nếu có lỗi khi gọi PayPal API
     */
    public Payment executePayment(
            String paymentId,
            String payerId
    ) throws PayPalRESTException {
        System.out.println("executePaymentexecutePaymentexecutePaymentexecutePaymentexecutePaymentexecutePaymentexecutePayment");
        // Tạo đối tượng Payment và thiết lập ID của thanh toán
        Payment payment = new Payment();
        payment.setId(paymentId);

        // Tạo đối tượng PaymentExecution để thực hiện thanh toán
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        System.out.println(payment.execute(apiContext, paymentExecution));
        return payment.execute(apiContext, paymentExecution);
    }
}
