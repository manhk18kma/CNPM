package kma.cnpm.beapp.domain.user.repository;

import kma.cnwat.be.domain.common.enumType.TokenType;
import kma.cnwat.be.domain.user.entity.ActiveResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ActiveResetTokenRepository extends JpaRepository<ActiveResetToken , Long> {

    @Query("SELECT a FROM ActiveResetToken a WHERE a.sub = :sub AND a.tokenType = :token_type")
    Optional<ActiveResetToken> findTokenBySub(@Param("sub") String sub, @Param("token_type") TokenType tokenType);
    @Modifying
    @Query("DELETE FROM ActiveResetToken a WHERE a.sub = :sub AND a.tokenType = :token_type")
    void deleteTokenBySub(@Param("sub") String sub, @Param("token_type") TokenType tokenType);


}
