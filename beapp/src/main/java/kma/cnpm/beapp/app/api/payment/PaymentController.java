package kma.cnpm.beapp.app.api.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kma.cnpm.beapp.app.service.RequestUtil;
import kma.cnpm.beapp.domain.common.dto.ResponseData;
import kma.cnpm.beapp.domain.payment.dto.request.DepositRequest;
import kma.cnpm.beapp.domain.payment.dto.response.DepositResponse;
import kma.cnpm.beapp.domain.payment.dto.response.PaypalResponse;
import kma.cnpm.beapp.domain.payment.dto.response.VnpayResponse;
import kma.cnpm.beapp.domain.payment.service.AccountService;
import kma.cnpm.beapp.domain.payment.service.PaymentService;
import kma.cnpm.beapp.domain.payment.service.PaypalService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PaymentController {
    PaymentService paymentService;
    PaypalService paypalService;
    @Operation(summary = "Deposit funds into an account", description = "Deposits the specified amount into the user's account")
    @PostMapping
    public  ResponseData<DepositResponse>  deposit(
            HttpServletRequest httpServletRequest,
            @Parameter(description = "Deposit request payload with account ID and amount", required = true)
            @RequestBody DepositRequest depositRequest) {
        var ipAddress = RequestUtil.getIpAddress(httpServletRequest);
        depositRequest.setIpAddress(ipAddress);
        DepositResponse response = paymentService.deposit(depositRequest);
        return new ResponseData<>(HttpStatus.CREATED.value(),
                "Deposit successful",
                new Date(),
                response);
    }

//    @GetMapping("/vnpay_ipn")
//    public void handleCallback(
//            @Parameter(description = "Callback data from VNPay", required = true)
//            @RequestParam Map<String, String> params) {
//        VnpayResponse vnpayResponse = new VnpayResponse(params);
//        log.info("[VNPay Ipn] Params: {}", params);
//        paymentService.handleCallback(vnpayResponse);
//    }




    @PostMapping("/vnpay_ipn")
    public void handleWebhook(@RequestBody String payload) {
        System.out.println("Received PayPal webhook event:");
        System.out.println(payload);

        try {
            // Tạo ObjectMapper để phân tích cú pháp JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(payload);

            // Kiểm tra giá trị của trường event_type
            String eventType = rootNode.path("event_type").asText();
            if ("PAYMENT.CANCELLED".equals(eventType)) {
                // Trích xuất thông tin từ payload
                String paymentId = rootNode.path("resource").path("id").asText();
                String reason = rootNode.path("resource").path("reason").asText(); // Nếu có thông tin về lý do hủy

                // Tạo đối tượng PaypalResponse
                PaypalResponse paypalResponse = new PaypalResponse();
                paypalResponse.setPaymentId(paymentId);
//                paypalResponse.setReason(reason);

                // In thông tin PaypalResponse
                System.out.println("Payment Cancelled:");
                System.out.println(paypalResponse);

                // Thực hiện các hành động cần thiết như cập nhật cơ sở dữ liệu
                // Ví dụ: cập nhật trạng thái thanh toán trong cơ sở dữ liệu
                // ...
            } else if ("PAYMENTS.PAYMENT.CREATED".equals(eventType)) {
                // Xử lý sự kiện thanh toán được tạo (nếu cần)
                String payerId = rootNode.path("resource").path("payer").path("payer_info").path("payer_id").asText();
                String paymentId = rootNode.path("resource").path("id").asText();
                String state = rootNode.path("resource").path("state").asText();
                String total = rootNode.path("resource").path("transactions").get(0).path("amount").path("total").asText();
                String currency = rootNode.path("resource").path("transactions").get(0).path("amount").path("currency").asText();

                // Tạo đối tượng PaypalResponse
                PaypalResponse paypalResponse = new PaypalResponse();
                paypalResponse.setPayerId(payerId);
                paypalResponse.setId(paymentId);
                paypalResponse.setState(state);
                paypalResponse.setTotal(total);
                paypalResponse.setCurrency(currency);
                paypalResponse.setPaymentId(paymentId);

                // In thông tin PaypalResponse
                System.out.println("Payment Created:");
                System.out.println(paypalResponse);
                paypalService.handleCallbackPayment(paypalResponse);
                // Thực hiện các hành động cần thiết như cập nhật cơ sở dữ liệu
                // ...
            } else {
                // Xử lý các loại sự kiện khác hoặc không xử lý
                System.out.println("Event type is not handled. Ignoring.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
