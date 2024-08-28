package kma.cnpm.beapp.domain.payment.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InitDepositRequest {
    private String requestId;

    private String ipAddress;

    private Long accountId;

    private Long txnRef;

    private long amount;
}
