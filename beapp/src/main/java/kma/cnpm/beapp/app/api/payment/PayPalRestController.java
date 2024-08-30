package kma.cnpm.beapp.app.api.payment;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import kma.cnpm.beapp.domain.common.upload.ImageService;
import kma.cnpm.beapp.domain.payment.service.FetchRating.ExchangeRate;
import kma.cnpm.beapp.domain.payment.service.FetchRating.ExchangeRateService;
import kma.cnpm.beapp.domain.payment.service.PaypalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
//@RequestMapping("/api/v1/paypals")
public class PayPalRestController {

    private final PaypalService paypalService;
    private final ExchangeRateService exchangeRateService;
    private final ImageService imageService;


    @GetMapping("/payment/success")
    public ResponseEntity<String> paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId
    ) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if ("approved".equals(payment.getState())) {
                return ResponseEntity.ok("Payment approved successfully.");
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while executing payment.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment not approved.");
    }

    @GetMapping("/payment/cancel")
    public ResponseEntity<String> paymentCancel() {
        return ResponseEntity.status(HttpStatus.OK).body("Payment was canceled.");
    }

    @GetMapping("/payment/error")
    public ResponseEntity<String> paymentError() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during payment.");
    }

    @PostMapping("/payment/create")
    public ResponseEntity<Object> createPayment(
            @RequestParam("method") String method,
            @RequestParam("amount") String amount,
            @RequestParam("currency") String currency,
            @RequestParam("description") String description
    ) {
//        ExchangeRate rates = exchangeRateService.getExchangeRates();
//        System.out.println(rates);
//        return null;
        try {
            String cancelUrl = "http://localhost:3000/payment/cancel";
            String successUrl = "http://localhost:3000/payment/success";
            Payment payment = paypalService.createPayment(
                    Double.valueOf(amount),
                    currency,
                    method,
                    "sale",
                    description,
                    cancelUrl,
                    successUrl
            );

            for (Links links : payment.getLinks()) {
                if ("approval_url".equals(links.getRel())) {
                    return ResponseEntity.ok(links.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create payment.");
    }
}


//
//package kma.cnpm.beapp.app.api.payment;
//
//import com.paypal.api.payments.Links;
//import com.paypal.api.payments.Payment;
//import com.paypal.base.rest.PayPalRESTException;
//import kma.cnpm.beapp.domain.payment.service.PaypalService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@Slf4j
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/paypal")
//public class PayPalRestController {
//
//    private final PaypalService paypalService; // Inject PaypalService để gọi các phương thức xử lý thanh toán
//
//    /**
//     * Xử lý khi người dùng trở lại ứng dụng sau khi thanh toán thành công trên PayPal.
//     *
//     * @param paymentId ID của thanh toán từ PayPal
//     * @param payerId ID của người thanh toán từ PayPal
//     * @return ResponseEntity với thông báo thành công hoặc lỗi
//     */
//    @GetMapping("/payment/success")
//    public ResponseEntity<String> paymentSuccess(
//            @RequestParam("paymentId") String paymentId,
//            @RequestParam("PayerID") String payerId
//    ) {
//        try {
//            // Xác thực thanh toán với PayPal
//            Payment payment = paypalService.executePayment(paymentId, payerId);
//            if ("approved".equals(payment.getState())) {
//                // Nếu thanh toán đã được chấp thuận, trả về thông báo thành công
//                return ResponseEntity.ok("Payment approved successfully.");
//            }
//        } catch (PayPalRESTException e) {
//            // Nếu có lỗi trong quá trình xác thực, ghi log lỗi và trả về thông báo lỗi
//            log.error("Error occurred:: ", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while executing payment.");
//        }
//        // Nếu thanh toán không được chấp thuận, trả về thông báo lỗi
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment not approved.");
//    }
//
//    /**
//     * Xử lý khi người dùng hủy thanh toán trên PayPal.
//     *
//     * @return ResponseEntity với thông báo thanh toán bị hủy
//     */
//    @GetMapping("/payment/cancel")
//    public ResponseEntity<String> paymentCancel() {
//        // Trả về thông báo thanh toán đã bị hủy
//        return ResponseEntity.status(HttpStatus.OK).body("Payment was canceled.");
//    }
//
//    /**
//     * Xử lý khi có lỗi xảy ra trong quá trình thanh toán.
//     *
//     * @return ResponseEntity với thông báo lỗi
//     */
//    @GetMapping("/payment/error")
//    public ResponseEntity<String> paymentError() {
//        // Trả về thông báo lỗi trong quá trình thanh toán
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during payment.");
//    }
//
//    /**
//     * Tạo yêu cầu thanh toán mới và trả về URL để người dùng xác nhận thanh toán trên PayPal.
//     *
//     * @param method Phương thức thanh toán (e.g., "paypal")
//     * @param amount Số tiền thanh toán
//     * @param currency Loại tiền tệ (e.g., "USD")
//     * @param description Mô tả thanh toán
//     * @return ResponseEntity với URL để xác nhận thanh toán trên PayPal hoặc thông báo lỗi
//     */
//    @PostMapping("/payment/create")
//    public ResponseEntity<Object> createPayment(
//            @RequestParam("method") String method,
//            @RequestParam("amount") String amount,
//            @RequestParam("currency") String currency,
//            @RequestParam("description") String description
//    ) {
//        try {
//            // URL để chuyển hướng người dùng nếu thanh toán bị hủy
//            String cancelUrl = "http://localhost:3000/payment/cancel";
//            // URL để chuyển hướng người dùng nếu thanh toán thành công
//            String successUrl = "http://localhost:3000/payment/success";
//
//            // Gọi dịch vụ PayPal để tạo thanh toán mới
//            Payment payment = paypalService.createPayment(
//                    Double.valueOf(amount),
//                    currency,
//                    method,
//                    "sale",
//                    description,
//                    cancelUrl,
//                    successUrl
//            );
//
//            // Lấy URL để người dùng xác nhận thanh toán trên PayPal
//            for (Links links : payment.getLinks()) {
//                if ("approval_url".equals(links.getRel())) {
//                    // Trả về URL để người dùng xác nhận thanh toán
//                    return ResponseEntity.ok(links.getHref());
//                }
//            }
//        } catch (PayPalRESTException e) {
//            // Nếu có lỗi trong quá trình tạo thanh toán, ghi log lỗi và trả về thông báo lỗi
//            log.error("Error occurred:: ", e);
//        }
//        // Nếu không thể tạo thanh toán, trả về thông báo lỗi
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create payment.");
//    }
//}
