package kma.cnpm.beapp.domain.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class BalanceDTO {
    BigDecimal balance;
    Long accountId;
}
