package kma.cnpm.beapp.domain.payment.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;

@Getter
@Setter
public class CreateWithdrawalRequest {

    @NotNull(message = "Mã tài khoản không được để trống")
    private Long accountHasBankId;

    @NotNull(message = "Số tiền không được để trống")
    @DecimalMin(value = "10000", message = "Số tiền phải lớn hơn 10000")
    private BigDecimal amount;
}