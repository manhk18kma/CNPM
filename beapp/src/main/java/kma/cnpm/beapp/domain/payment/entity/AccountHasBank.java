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

    public void addAccountHasBank(Account account, Bank bank) {
        if (this.account != null && this.bank != null) {
            this.account.getAccountHasBanks().remove(this);
            this.bank.getAccountHasBanks().remove(this);
        }
        this.account = account;
        this.bank = bank;

        if (account != null && !account.getAccountHasBanks().contains(this)) {
            account.getAccountHasBanks().add(this);
        }
        if (bank != null && !bank.getAccountHasBanks().contains(this)) {
            bank.getAccountHasBanks().add(this);
        }
    }

    public void addWithdrawals(Withdrawal withdrawal) {
        if (this.withdrawals == null) {
            this.withdrawals = new HashSet<>();
        }
        this.withdrawals.add(withdrawal);
        withdrawal.setAccountHasBank(this);
    }


}
