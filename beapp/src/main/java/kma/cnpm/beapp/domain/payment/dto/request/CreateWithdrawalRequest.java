package kma.cnpm.beapp.domain.payment.dto.request;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CreateWithdrawalRequest {
    private Long accountHasBankId;
    private BigDecimal amount;
}
