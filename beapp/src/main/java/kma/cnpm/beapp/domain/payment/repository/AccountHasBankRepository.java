package kma.cnpm.beapp.domain.payment.repository;

import kma.cnpm.beapp.domain.payment.entity.Account;
import kma.cnpm.beapp.domain.payment.entity.AccountHasBank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountHasBankRepository  extends JpaRepository<AccountHasBank, Long> {

}
