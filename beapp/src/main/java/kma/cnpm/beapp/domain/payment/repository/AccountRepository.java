package kma.cnpm.beapp.domain.payment.repository;

import kma.cnpm.beapp.domain.payment.entity.Account;
import kma.cnpm.beapp.domain.payment.entity.PaymentGateway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account , Long> {
    @Query("SELECT a FROM Account a WHERE a.userId = :userId")
    Optional<Account> findAccountByUserId(@Param("userId") Long userId);


    @Query("SELECT p FROM PaymentGateway p WHERE p.id = :id")
    Optional<PaymentGateway> findPaymentGatewayById(@Param("id") Long id);

}
