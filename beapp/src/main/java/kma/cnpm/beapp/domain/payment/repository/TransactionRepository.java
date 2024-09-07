package kma.cnpm.beapp.domain.payment.repository;

import kma.cnpm.beapp.domain.payment.entity.Bank;
import kma.cnpm.beapp.domain.payment.entity.PaypalTransaction;
import kma.cnpm.beapp.domain.payment.entity.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.id =:id")
    Optional<Transaction> findTranById(@Param("id") Long id);


    @Query("SELECT p FROM PaypalTransaction p WHERE p.paymentId =:paymentId")
    Optional<PaypalTransaction> findTransactionByPaymentId(@Param("paymentId") String paymentId);
}
