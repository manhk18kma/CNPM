package kma.cnpm.beapp.app.api.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kma.cnpm.beapp.domain.common.dto.ResponseData;
import kma.cnpm.beapp.domain.payment.dto.request.AddBankRequest;
import kma.cnpm.beapp.domain.payment.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/accounts")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AccountController {
    AccountService accountService;

    @Operation(
            summary = "Add Bank to Account",
            description = "Add a new bank to the user's account. Call this API with BEARER TOKEN for authentication."
    )
    @PostMapping("/banks")
    public ResponseData<?> addBankToAccount(
            @Parameter(description = "Request payload containing bank details to add", required = true)
            @RequestBody @Valid AddBankRequest request) {
        accountService.addBankToAccount(request);
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Ngân hàng đã được thêm thành công",
                new Date()
        );
    }

    @Operation(
            summary = "Remove Bank from Account",
            description = "Remove a bank from the user's account. Call this API with BEARER TOKEN for authentication."
    )
    @DeleteMapping("/banks/{id}")
    public ResponseData removeBankAccount(
            @Parameter(description = "The unique identifier of the bank to be removed", required = true)
            @PathVariable @NotNull Long id) {
        accountService.removeBankAccount(id);
        return new ResponseData<>(
                HttpStatus.NO_CONTENT.value(),
                "Ngân hàng đã được xóa thành công",
                new Date()
        );
    }
}
