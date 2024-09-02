package kma.cnpm.beapp.domain.payment.entity;

import jakarta.persistence.*;
import kma.cnpm.beapp.domain.common.enumType.Currency;
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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "transaction_type")
public class Transaction extends AbstractEntity<Long>{

    @Column(name = "amount" , precision = 19, scale = 3)
    private BigDecimal amount;

    @Column(name = "status", length = 20)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "ip_address")
    private String ipAddress;


    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;


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
        if(this.transactionHistories==null){
            this.transactionHistories = new HashSet<>();
        }
        transactionHistory.setTransaction(this);
        this.transactionHistories.add(transactionHistory);
    }
}

