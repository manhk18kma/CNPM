package kma.cnpm.beapp.domain.payment.entity;

import jakarta.persistence.*;
import kma.cnpm.beapp.domain.common.enumType.TransactionStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_paypal_transactions")
public class PaypalTransaction  extends AbstractEntity<Long>{

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "payer_id")
    private String payerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TransactionStatus status;

    @Column(name = "amount", precision = 19, scale = 3)
    private BigDecimal amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "usd_to_eur")
    private BigDecimal usdToEur;

    @Column(name = "usd_to_vnd")
    private BigDecimal usdToVnd;

    @Column(name = "ip_address")
    private String ipAddress;

    //    Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_gateway_id")
    private PaymentGateway paymentGateway;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;




    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "paypalTransaction")
    private Set<PaypalTransactionHistory> paypalTransactionHistories = new HashSet<>();
}
