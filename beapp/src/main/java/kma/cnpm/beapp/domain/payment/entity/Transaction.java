package kma.cnpm.beapp.domain.payment.entity;

import jakarta.persistence.*;
import kma.cnpm.beapp.domain.common.enumType.TransactionStatus;
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
@Table(name = "tbl_transactions")
public class Transaction extends AbstractEntity<Long>{

    @Column(name = "amount" , precision = 19, scale = 3)
    private BigDecimal amount;


    @Column(name = "ip_address")
    private String ipAddress;

    //    Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_gateway_id")
    private PaymentGateway paymentGateway;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "transaction")
    private Set<TransactionHistory> transactionHistories = new HashSet<>();

    public void addStatus(TransactionHistory transactionHistory) {
        transactionHistory.setTransaction(this);
        transactionHistories.add(transactionHistory);
    }
}

