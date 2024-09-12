package kma.cnpm.beapp.domain.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
public class AccountResponse implements Serializable {
    private Long userId;
    private Long accountId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long withdrawalId;

}
