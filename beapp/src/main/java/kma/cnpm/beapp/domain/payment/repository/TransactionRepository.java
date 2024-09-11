package kma.cnpm.beapp.domain.payment.repository;

import kma.cnpm.beapp.domain.common.enumType.TransactionStatus;
import kma.cnpm.beapp.domain.common.enumType.WithdrawalStatus;
import kma.cnpm.beapp.domain.payment.entity.Bank;
import kma.cnpm.beapp.domain.payment.entity.PaypalTransaction;
import kma.cnpm.beapp.domain.payment.entity.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.id =:id")
    Optional<Transaction> findTranById(@Param("id") Long id);


    @Query("SELECT p FROM PaypalTransaction p WHERE p.paymentId =:paymentId")
    Optional<PaypalTransaction> findTransactionByPaymentId(@Param("paymentId") String paymentId);


    @Query("SELECT t FROM Transaction t WHERE t.account.userId = :userId AND TYPE(t) = Transaction AND (:status IS NULL OR t.status = :status)")
    Page<Transaction> getVnpayTransactionInfoOfUser(@Param("userId") Long userId, @Param("status") TransactionStatus status, Pageable pageable);

    // Truy vấn cho PaypalTransaction
    @Query("SELECT t FROM Transaction t WHERE t.account.userId = :userId AND TYPE(t) = PaypalTransaction AND (:status IS NULL OR t.status = :status)")
    Page<PaypalTransaction> getPaypalTransactionInfoOfUser(@Param("userId") Long userId, @Param("status") TransactionStatus status, Pageable pageable);

    // Truy vấn tổng quan cho tất cả loại giao dịch
    @Query("SELECT t FROM Transaction t WHERE t.account.userId = :userId AND (:status IS NULL OR t.status = :status)")
    Page<Transaction> getTransactionInfoOfUser(@Param("userId") Long userId, @Param("status") TransactionStatus status, Pageable pageable);

}
