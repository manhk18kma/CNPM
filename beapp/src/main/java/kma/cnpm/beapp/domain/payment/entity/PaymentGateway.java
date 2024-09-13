package kma.cnpm.beapp.domain.payment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_payment_gateways")
public class PaymentGateway extends AbstractEntity<Long>{

    @Column(name = "name")
    private String name;

    @Column(name = "provider")
    private String provider;

    @Column(name = "fee_rate", precision = 5, scale = 2)
    private BigDecimal feeRate;

    @Column(name = "payment_gateway_avt")
    private String paymentGatewayAvt;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "paymentGateway")
    private Set<Transaction> transactions = new HashSet<>();

}
