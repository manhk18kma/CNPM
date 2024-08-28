package kma.cnpm.beapp.domain.payment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_account_bank")
public class AccountHasBank  extends  AbstractEntity<Long>{

    @Column(name = "account_number")
    private String accountNumber;
    //    Relationships
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @OneToMany(mappedBy = "accountHasBank")
    private Set<Withdrawal> withdrawals = new HashSet<>();
}
