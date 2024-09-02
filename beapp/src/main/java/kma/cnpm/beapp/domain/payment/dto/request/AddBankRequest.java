package kma.cnpm.beapp.domain.payment.dto.request;

import lombok.Getter;

@Getter
public class AddBankRequest {
    private Long bankId;
    private String accountNumber;
}
