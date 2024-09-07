package kma.cnpm.beapp.domain.payment.repository;

import kma.cnpm.beapp.domain.payment.entity.AccountHasBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountHasBankRepository  extends JpaRepository<AccountHasBank, Long> {

    @Query("SELECT CASE WHEN COUNT(ac) > 0 THEN true ELSE false END FROM AccountHasBank ac " +
            "WHERE ac.account.id = :accountId AND ac.bank.id = :bankId")
    boolean existsAccountHasBankByAccountIdAndBankId(@Param("accountId") Long accountId, @Param("bankId") Long bankId);

    @Query("SELECT ahb FROM AccountHasBank ahb WHERE ahb.id = :id")
    Optional<AccountHasBank> findByIdOptional(@Param("id") Long id);


    @Modifying
    @Query("DELETE FROM AccountHasBank ahb WHERE ahb.id = :id")
    void deleteByIdCus(@Param("id") Long id);

}
