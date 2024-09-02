package kma.cnpm.beapp.domain.payment.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DepositResponseVnPay {
    Long idTransaction;
    String vnpUrl;
}
