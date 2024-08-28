package kma.cnpm.beapp.domain.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DepositRequest {
    @NotBlank(message = "Request ID is mandatory")
    private String requestId;
    @Positive(message = "Amount must be greater than zero")
    private Long amount;
    private String ipAddress;
    private Long paymentGatewayId;
}
