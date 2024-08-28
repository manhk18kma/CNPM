package kma.cnpm.beapp.domain.payment.repository;

import kma.cnpm.beapp.domain.payment.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentGatewayRepository extends JpaRepository<Bank, Long> {
}
