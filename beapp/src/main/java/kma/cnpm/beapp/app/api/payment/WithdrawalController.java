package kma.cnpm.beapp.app.api.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kma.cnpm.beapp.domain.common.dto.ResponseData;
import kma.cnpm.beapp.domain.payment.dto.request.AddBankRequest;
import kma.cnpm.beapp.domain.payment.dto.request.CreateWithdrawalRequest;
import kma.cnpm.beapp.domain.payment.dto.response.AccountResponse;
import kma.cnpm.beapp.domain.payment.entity.Withdrawal;
import kma.cnpm.beapp.domain.payment.service.WithdrawalService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/withdrawals")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WithdrawalController {
    WithdrawalService withdrawalService;
    @Operation(summary = "Create Withdrawal Request", description = "Create a withdrawal request for the user's account. Call this API with BEARER TOKEN for authentication.")
    @PostMapping()
    public ResponseData<AccountResponse> createWithdrawal(
            @Parameter(description = "Request payload containing details for withdrawal", required = true)
            @RequestBody @Valid CreateWithdrawalRequest request) {
        AccountResponse response = withdrawalService.createWithdrawal(request);
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Withdrawal request created successfully",
                new Date(),
                response
        );
    }

    @Operation(summary = "Cancel Withdrawal", description = "Cancel an existing withdrawal request. Call this API with BEARER TOKEN for authentication.")
    @DeleteMapping("/{id}")
    public ResponseData<Void> cancelWithdrawal(
            @Parameter(description = "The unique identifier of the withdrawal request to be canceled", required = true)
            @PathVariable @NotNull Long id) {
        withdrawalService.cancelWithdrawal(id);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Withdrawal request canceled successfully",
                new Date()
        );
    }


    @Operation(summary = "Approve Withdrawal", description = "Approve an existing withdrawal request. Call this API with BEARER TOKEN for authentication.")
    @PutMapping("/{id}")
    public ResponseData<Void> approve(
            @Parameter(description = "The unique identifier of the withdrawal request to be approved", required = true)
            @PathVariable @NotNull Long id) {
        withdrawalService.approveWithdrawal(id);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Withdrawal request approved successfully",
                new Date()
        );
    }



}
