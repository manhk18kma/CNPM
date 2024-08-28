package kma.cnpm.beapp.domain.payment.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DepositResponse {
    Long idTransaction;
    String vnpUrl;
}
