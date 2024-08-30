package kma.cnpm.beapp.domain.payment.repository;

import kma.cnpm.beapp.domain.payment.entity.PaypalTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaypalTransactionRepository extends JpaRepository<PaypalTransaction , String> {
}
