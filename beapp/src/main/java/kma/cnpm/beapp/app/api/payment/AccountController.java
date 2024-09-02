package kma.cnpm.beapp.app.api.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import kma.cnpm.beapp.domain.common.dto.ResponseData;
import kma.cnpm.beapp.domain.payment.dto.request.AddBankRequest;
import kma.cnpm.beapp.domain.payment.dto.response.AccountResponse;
import kma.cnpm.beapp.domain.payment.entity.Account;
import kma.cnpm.beapp.domain.payment.service.AccountService;
import kma.cnpm.beapp.domain.user.dto.response.UserResponse;
import kma.cnpm.beapp.domain.user.dto.resquest.AddAddressRequest;
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

    @Operation(summary = "Add Bank to Account", description = "Add a new bank to the user's account. Call this API with BEARER TOKEN for authentication.")
    @PostMapping("/banks")
    public ResponseData<AccountResponse> addBankToAccount(
            @Parameter(description = "Request payload containing bank details to add", required = true)
            @RequestBody @Valid AddBankRequest request) {
        AccountResponse response = accountService.addBankToAccount(request);
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Bank added successfully",
                new Date(),
                response
        );
    }

    @Operation(summary = "Remove Bank from Account", description = "Remove a bank from the user's account. Call this API with BEARER TOKEN for authentication.")
    @DeleteMapping("/banks/{id}")
    public ResponseData removeBankAccount(
            @Parameter(description = "The unique identifier of the bank to be removed", required = true)
            @PathVariable Long id) {
      accountService.removeBankAccount(id);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Bank removed successfully",
                new Date()
        );
    }


}
