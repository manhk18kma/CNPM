package kma.cnpm.beapp.app.api.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kma.cnpm.beapp.domain.common.dto.PageResponse;
import kma.cnpm.beapp.domain.common.dto.ResponseData;
import kma.cnpm.beapp.domain.common.enumType.WithdrawalSort;
import kma.cnpm.beapp.domain.common.enumType.WithdrawalStatus;
import kma.cnpm.beapp.domain.common.validation.EnumValue;
import kma.cnpm.beapp.domain.payment.dto.request.CreateWithdrawalRequest;
import kma.cnpm.beapp.domain.payment.dto.response.AccountResponse;
import kma.cnpm.beapp.domain.payment.dto.response.WithdrawalResponse;
import kma.cnpm.beapp.domain.payment.service.WithdrawalService;
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

@RestController
@RequestMapping("/api/v1/withdrawals")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WithdrawalController {
    WithdrawalService withdrawalService;

    @Operation(
            summary = "Create Withdrawal Request",
            description = "Create a withdrawal request for the user's account. Call this API with BEARER TOKEN for authentication."
    )
    @PostMapping()
    public ResponseData<?> createWithdrawal(
            @Parameter(description = "Request payload containing details for withdrawal", required = true)
            @RequestBody @Valid CreateWithdrawalRequest request) {
        AccountResponse response =  withdrawalService.createWithdrawal(request);
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Yêu cầu rút tiền đã được tạo thành công",
                LocalDateTime.now(),
                response
        );
    }

    @Operation(
            summary = "Cancel Withdrawal",
            description = "Cancel an existing withdrawal request. Call this API with BEARER TOKEN for authentication."
    )
    @DeleteMapping("/{id}")
    public ResponseData<?> cancelWithdrawal(
            @Parameter(description = "The unique identifier of the withdrawal request to be canceled", required = true)
            @PathVariable @NotNull Long id) {
        AccountResponse response =  withdrawalService.cancelWithdrawal(id);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Yêu cầu rút tiền đã được hủy thành công",
                LocalDateTime.now(),response
        );
    }

    @Operation(
            summary = "Reject Withdrawal",
            description = "Reject an existing withdrawal request. This action can only be performed by an admin."
    )
    @DeleteMapping("/admin/{id}")
    public ResponseData<?> rejectWithdrawal(
            @Parameter(description = "The unique identifier of the withdrawal request to be rejected", required = true)
            @PathVariable @NotNull Long id) {
        AccountResponse response = withdrawalService.rejectWithdrawal(id);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Yêu cầu rút tiền đã bị từ chối thành công",
                LocalDateTime.now(),
                response
        );
    }

    @Operation(
            summary = "Approve Withdrawal",
            description = "Approve an existing withdrawal request. This API is intended for employee implementation later."
    )
    @PutMapping("/{id}")
    public ResponseData<Void> approve(
            @Parameter(description = "The unique identifier of the withdrawal request to be approved", required = true)
            @PathVariable @NotNull Long id) {
        withdrawalService.approveWithdrawal(id);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Yêu cầu rút tiền đã được chấp thuận thành công",
                LocalDateTime.now()
        );
    }

    @GetMapping
    @Operation(
            summary = "Get withdrawals of a user",
            description = "Returns a paginated list of user withdrawals filtered by status and sorted by the specified field. Call this API with BEARER TOKEN for authentication."
    )
    public ResponseData<PageResponse<List<WithdrawalResponse>>> getWithdrawalsOfUser(
            @RequestParam(defaultValue = "DEFAULT") @EnumValue(name = "status", enumClass = WithdrawalStatus.class) String status,
            @RequestParam(defaultValue = "CREATE_DESC") @EnumValue(name = "sortBy", enumClass = WithdrawalSort.class) String sortBy) {
        PageResponse<List<WithdrawalResponse>> response = withdrawalService.getWithdrawalsOfUser(status, sortBy, 0, 100);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Danh sách yêu cầu rút tiền đã được lấy thành công",
                LocalDateTime.now(),
                response);
    }


    //for admin get list withdrawl of users
    @GetMapping("/admin")
    @Operation(
            summary = "Get withdrawals of all users (Admin)",
            description = "Returns a paginated list of all user withdrawals filtered by status and sorted by the specified field. This API is intended for admins."
    )
    public ResponseData<PageResponse<List<WithdrawalResponse>>> getWithdrawalsOfAllUsers(
            @RequestParam(defaultValue = "DEFAULT") @EnumValue(name = "status", enumClass = WithdrawalStatus.class) String status,
            @RequestParam(defaultValue = "CREATE_DESC") @EnumValue(name = "sortBy", enumClass = WithdrawalSort.class) String sortBy) {
        PageResponse<List<WithdrawalResponse>> response = withdrawalService.getWithdrawalsOfAllUsers(status, sortBy, 0, 100);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Danh sách yêu cầu rút tiền đã được lấy thành công",
                LocalDateTime.now(),
                response
        );
    }
}
