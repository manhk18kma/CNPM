package kma.cnpm.beapp.domain.payment.repository;

import kma.cnpm.beapp.domain.payment.entity.Bank;
import kma.cnpm.beapp.domain.payment.entity.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {
}
