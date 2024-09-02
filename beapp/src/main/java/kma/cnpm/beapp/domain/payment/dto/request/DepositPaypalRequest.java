package kma.cnpm.beapp.domain.payment.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;
@Getter
public class DepositPaypalRequest {
    private BigDecimal amount;

    @NotNull(message = "Payment Gateway ID cannot be null")
    private Long paymentGatewayId;
}
