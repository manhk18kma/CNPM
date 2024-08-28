package kma.cnpm.beapp.domain.payment.repository;

import kma.cnpm.beapp.domain.payment.entity.Bank;
import kma.cnpm.beapp.domain.payment.entity.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
}
