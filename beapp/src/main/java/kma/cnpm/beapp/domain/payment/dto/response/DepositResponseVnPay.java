package kma.cnpm.beapp.domain.payment.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DepositResponseVnPay {
    private Long userId;
    private Long accountId;
    Long transactionId;
    String vnpUrl;
}
