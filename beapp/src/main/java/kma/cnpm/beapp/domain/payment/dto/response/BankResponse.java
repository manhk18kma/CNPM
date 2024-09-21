package kma.cnpm.beapp.domain.payment.dto.response;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BankResponse {
    private Long bankId;

    private String bankName;

    private String bankAvt;

    private String bankCode;
}
