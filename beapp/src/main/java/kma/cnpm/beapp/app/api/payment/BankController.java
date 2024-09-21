package kma.cnpm.beapp.app.api.payment;

import io.swagger.v3.oas.annotations.Operation;
import kma.cnpm.beapp.domain.common.dto.PageResponse;
import kma.cnpm.beapp.domain.common.dto.ResponseData;

import kma.cnpm.beapp.domain.payment.dto.response.BankResponse;
import kma.cnpm.beapp.domain.payment.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/banks")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BankController {
    AccountService accountService;

    @GetMapping
    @Operation(
            summary = "Get list banks",
            description = "Get list banks"
    )
    public ResponseData<PageResponse<List<BankResponse>>> getBanks() {
        PageResponse<List<BankResponse>> response = accountService.getBanks();
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Lấy danh sách ngân hàng thành công",
                LocalDateTime.now(),
                response);
    }


}