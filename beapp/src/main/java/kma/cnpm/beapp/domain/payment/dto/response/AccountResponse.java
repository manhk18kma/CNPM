package kma.cnpm.beapp.domain.payment.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
public class AccountResponse implements Serializable {
    private Long accountId;

}
