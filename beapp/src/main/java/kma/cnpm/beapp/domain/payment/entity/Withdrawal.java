package kma.cnpm.beapp.domain.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import kma.cnpm.beapp.domain.common.enumType.WithdrawalStatus;

@Setter
@Getter
@Entity
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_withdrawal_requests")
public class Withdrawal extends AbstractEntity<Long> {
    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "amount" , precision = 19, scale = 3)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private WithdrawalStatus status;

//    Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_has_bank_id")
    private AccountHasBank accountHasBank;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "transaction_id")
//    private Transaction transaction;

}
