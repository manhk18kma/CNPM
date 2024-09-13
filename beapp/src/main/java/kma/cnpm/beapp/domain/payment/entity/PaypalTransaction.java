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
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PaypalTransaction  extends Transaction{

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "payer_id")
    private String payerId;

    @Column(name = "usd_to_eur")
    private BigDecimal usdToEur;

    @Column(name = "usd_to_vnd")
    private BigDecimal usdToVnd;

    @Column(name = "amount_in_usd" , precision = 19, scale = 3)
    private BigDecimal amountInUsd;


}
