package kma.cnpm.beapp.app.api.payment;

import com.paypal.base.rest.PayPalRESTException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kma.cnpm.beapp.app.service.RequestUtil;
import kma.cnpm.beapp.domain.common.dto.PageResponse;
import kma.cnpm.beapp.domain.common.dto.ResponseData;
import kma.cnpm.beapp.domain.common.enumType.TransactionStatus;
import kma.cnpm.beapp.domain.common.enumType.TransactionType;
import kma.cnpm.beapp.domain.common.enumType.WithdrawalSort;
import kma.cnpm.beapp.domain.common.validation.EnumValue;
import kma.cnpm.beapp.domain.payment.dto.request.DepositPaypalRequest;
import kma.cnpm.beapp.domain.payment.dto.request.DepositVnPayRequest;
import kma.cnpm.beapp.domain.payment.dto.response.*;
import kma.cnpm.beapp.domain.payment.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transactions")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PaymentController {
    PaymentService paymentService;

    @Operation(
            summary = "Deposit funds into an account using VNPAY",
            description = "Deposits the specified amount into the user's account using VNPAY. Call this API with BEARER TOKEN for authentication."
    )
    @PostMapping("/vnpay")
    public ResponseData<DepositResponseVnPay> depositVnPay(
            HttpServletRequest httpServletRequest,
            @Parameter(description = "Deposit request payload with account ID and amount", required = true)
            @RequestBody @Valid DepositVnPayRequest depositRequest) {
        var ipAddress = RequestUtil.getIpAddress(httpServletRequest);
        System.out.println(ipAddress);
        DepositResponseVnPay response = paymentService.depositVnPay(depositRequest, ipAddress);
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Nạp tiền thành công",
                LocalDateTime.now(),
                response);
    }

    @GetMapping("/vnpay_ipn")
    public void handleCallback(
            @Parameter(description = "Callback data from VNPay", required = true)
            @RequestParam Map<String, String> params) {
        log.info("[VNPay Ipn] Params: {}", params);
        paymentService.handleCallbackVNPay(params);
    }

    @Operation(
            summary = "Deposit funds into an account using PayPal",
            description = "Deposits the specified amount into the user's account using PayPal. Call this API with BEARER TOKEN for authentication."
    )
    @PostMapping("/paypal")
    public ResponseData<DepositResponsePaypal> depositPaypal(
            HttpServletRequest httpServletRequest,
            @Parameter(description = "Deposit request payload with account ID and amount", required = true)
            @RequestBody @Valid DepositPaypalRequest request) throws PayPalRESTException {
        var ipAddress = RequestUtil.getIpAddress(httpServletRequest);
        DepositResponsePaypal response = paymentService.depositPaypal(request, ipAddress);
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Nạp tiền thành công",
                LocalDateTime.now(),
                response);
    }

    @PostMapping("/paypal_ipn")
    public void handleWebhook(
            @Parameter(description = "Callback data from PayPal", required = true)
            @RequestBody String payload) throws PayPalRESTException {
        paymentService.handleCallbackPaypal(payload);
    }

    @GetMapping
    @Operation(
            summary = "Get payments of a user",
            description = "Returns a paginated list of user payments filtered by status and sorted by the specified field. Call this API with BEARER TOKEN for authentication."
    )
    public ResponseData<PageResponse<List<TransactionResponse>>> getTransactionInfoOfUser(
            @RequestParam(defaultValue = "DEFAULT") @EnumValue(name = "status", enumClass = TransactionStatus.class) String status,
            @RequestParam(defaultValue = "CREATE_DESC") @EnumValue(name = "sortBy", enumClass = WithdrawalSort.class) String sortBy,
            @RequestParam(defaultValue = "DEFAULT") @EnumValue(name = "type", enumClass = TransactionType.class) String type) {
        PageResponse<List<TransactionResponse>> response = paymentService.getTransactionInfoOfUser(status, sortBy, type, 0, 100);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Lấy thông tin giao dịch thành công",
                LocalDateTime.now(),
                response);
    }
}
