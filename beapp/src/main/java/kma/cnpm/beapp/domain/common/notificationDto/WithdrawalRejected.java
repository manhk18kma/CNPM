package kma.cnpm.beapp.domain.common.notificationDto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class WithdrawalRejected {
    private Long userId;
    private BigDecimal amount;
    private Long withdrawalId;
}
