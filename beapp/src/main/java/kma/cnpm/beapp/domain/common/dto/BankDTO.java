package kma.cnpm.beapp.domain.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class BankDTO {
    private Long accountHasBankId;
    private String bankCode;
    private String bankName;
    private String bankNumber;
    private String bankAvt;

}
