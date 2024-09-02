package kma.cnpm.beapp.domain.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepositResponsePaypal implements Serializable {
    @JsonProperty("idTransaction")
    private Long idTransaction;

    @JsonProperty("paypalUrl")
    private String paypalUrl;

    private String cancelUrl;

    private String successUrl;
}
