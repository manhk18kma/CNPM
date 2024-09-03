package kma.cnpm.beapp.domain.payment.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositVnPayRequest {

    @NotNull(message = "Số tiền không được để trống")
    @Min(value = 10000, message = "Số tiền phải lớn hơn hoặc bằng 10.000")
    private Long amount;

    @NotNull(message = "Mã cổng thanh toán không được để trống")
    private Long paymentGatewayId;
}
