package kma.cnpm.beapp.domain.payment.dto.response;

import jakarta.persistence.*;
import kma.cnpm.beapp.domain.common.enumType.Currency;
import kma.cnpm.beapp.domain.common.enumType.TransactionStatus;
import kma.cnpm.beapp.domain.payment.entity.Account;
import kma.cnpm.beapp.domain.payment.entity.PaymentGateway;
import kma.cnpm.beapp.domain.payment.entity.TransactionHistory;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
public class VnpayTransactionResponse {

    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private BigDecimal amountInVnd;

    private TransactionStatus status;

    private Currency currency;

    private PaymentGateway paymentGateway;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "transaction")
    private Set<TransactionHistory> transactionHistories = new HashSet<>();

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
