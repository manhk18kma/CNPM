package kma.cnpm.beapp.domain.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaypalTransactionHistory extends JpaRepository<kma.cnpm.beapp.domain.payment.entity.PaypalTransactionHistory , Long> {
}
