package kma.cnpm.beapp.domain.payment.entity;

import jakarta.persistence.*;
import kma.cnpm.beapp.domain.common.enumType.TransactionStatus;
import lombok.*;

@Setter
@Getter
@Entity
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_transaction_status_history")
public class TransactionHistory extends AbstractEntity<Long>{
    @Enumerated(EnumType.STRING)
    @Column(name = "new_status")
    private TransactionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

}

