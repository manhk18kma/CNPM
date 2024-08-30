package kma.cnpm.beapp.domain.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_account")
public class Account extends AbstractEntity<Long> {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "balance", precision = 19, scale = 3)
    private BigDecimal balance;

//    Relationships
    @OneToMany(mappedBy = "account")
    private Set<AccountHasBank> accountHasBanks = new HashSet<>();

    @OneToMany(mappedBy = "account")
    private Set<Withdrawal> withdrawals = new HashSet<>();

    @OneToMany(mappedBy = "account")
    private Set<Transaction> transactions = new HashSet<>();


    @OneToMany(mappedBy = "account")
    private Set<PaypalTransaction> paypalTransactions = new HashSet<>();
}
