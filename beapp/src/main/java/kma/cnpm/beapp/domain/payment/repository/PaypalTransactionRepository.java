package kma.cnpm.beapp.domain.payment.repository;

import kma.cnpm.beapp.domain.payment.entity.PaypalTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaypalTransactionRepository extends JpaRepository<PaypalTransaction , String> {

    @Query("select p FROM PaypalTransaction p WHERE p.paymentId =:paymentId")
    Optional<PaypalTransaction> findPaypalTransactionByPaymentId(@Param("paymentId") String paymentId);
}
