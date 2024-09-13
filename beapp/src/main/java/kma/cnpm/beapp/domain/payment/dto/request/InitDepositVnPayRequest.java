package kma.cnpm.beapp.domain.payment.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InitDepositVnPayRequest {
    private String requestId;

    private String ipAddress;

    private Long accountId;

    private Long txnRef;

    private Long  amount;
}
