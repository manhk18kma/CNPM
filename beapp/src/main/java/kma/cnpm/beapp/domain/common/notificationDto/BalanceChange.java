package kma.cnpm.beapp.domain.common.notificationDto;

import kma.cnpm.beapp.domain.common.enumType.NotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Builder
@Getter
@Setter
public class BalanceChange {
    Long userId;
    BigDecimal amount;
    BigDecimal balance;
    Long transactionId;
    NotificationType notificationType;
    boolean plusOrMinus;
    String balanceChangeImg;
}
