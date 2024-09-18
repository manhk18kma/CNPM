package kma.cnpm.beapp.domain.payment.repository;

import kma.cnpm.beapp.domain.common.enumType.WithdrawalStatus;
import kma.cnpm.beapp.domain.payment.entity.Bank;
import kma.cnpm.beapp.domain.payment.entity.Withdrawal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {
    @Query("SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END " +
            "FROM Withdrawal w WHERE w.account.id = :accountId AND w.status = 'PENDING'")
    boolean existsPendingWithdrawal(@Param("accountId") Long accountId);



    @Query("SELECT w FROM Withdrawal w WHERE w.id = :id")
    Optional<Withdrawal> findWithdrawalById(@Param("id") Long id);

    @Query("SELECT w FROM Withdrawal w WHERE w.account.userId = :userId AND (:status IS NULL OR w.status = :status)")
    Page<Withdrawal> getWithdrawalsOfUser(@Param("userId") Long userId, @Param("status") WithdrawalStatus status, Pageable pageable);




}
