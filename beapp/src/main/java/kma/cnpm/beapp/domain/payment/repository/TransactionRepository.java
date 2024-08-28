package kma.cnpm.beapp.domain.payment.repository;

import kma.cnpm.beapp.domain.payment.entity.Bank;
import kma.cnpm.beapp.domain.payment.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
