package kma.cnpm.beapp.app.api.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kma.cnpm.beapp.app.service.RequestUtil;
import kma.cnpm.beapp.domain.common.dto.ResponseData;
import kma.cnpm.beapp.domain.payment.dto.request.DepositRequest;
import kma.cnpm.beapp.domain.payment.dto.response.DepositResponse;
import kma.cnpm.beapp.domain.payment.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/vnpay_ipn")
    public String handleCallback(
            @Parameter(description = "Callback data from VNPay", required = true)
            @RequestParam Map<String, String> params) {
        log.info("[VNPay Ipn] Params: {}", params);
        return "1";
    }

}
