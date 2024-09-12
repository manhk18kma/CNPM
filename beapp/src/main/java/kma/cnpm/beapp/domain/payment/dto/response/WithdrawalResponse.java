package kma.cnpm.beapp.domain.payment.dto.response;

import jakarta.persistence.*;
import kma.cnpm.beapp.domain.common.enumType.WithdrawalStatus;
import kma.cnpm.beapp.domain.payment.entity.Account;
import kma.cnpm.beapp.domain.payment.entity.AccountHasBank;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
public class WithdrawalResponse implements Serializable {
    private Long userId;
    private Long accountId;
    private BigDecimal amount;
    private WithdrawalStatus status;
    private String accountNumber;
    private String bankName;
    private String bankAvt;
    private String bankCode;
    private LocalDateTime createdAt;
}
