package kma.cnpm.beapp.domain.payment.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositVnPayRequest {

    private Long amount;

    @NotNull(message = "Payment Gateway ID cannot be null")
    private Long paymentGatewayId;
}
