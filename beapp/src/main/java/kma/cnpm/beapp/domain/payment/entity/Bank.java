package kma.cnpm.beapp.domain.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "tbl_bank")
public class Bank extends AbstractEntity<Long> {

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_avt")
    private String bankAvt;

    @Column(name = "bank_code")
    private String bankCode;

    @OneToMany(mappedBy = "bank")
    private Set<AccountHasBank> accountHasBanks = new HashSet<>();
}
