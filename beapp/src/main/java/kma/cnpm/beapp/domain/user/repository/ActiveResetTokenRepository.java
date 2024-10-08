package kma.cnpm.beapp.domain.user.repository;

import kma.cnpm.beapp.domain.common.enumType.TokenType;
import kma.cnpm.beapp.domain.user.entity.ActiveResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ActiveResetTokenRepository extends JpaRepository<ActiveResetToken , Long> {

    @Query("SELECT a FROM ActiveResetToken a WHERE a.jwtId = :jwtID AND a.sub = :sub" +
            " AND a.tokenType = :token_type")
    Optional<ActiveResetToken> findResetTokenBySubAndJwtId(@Param("jwtID") String jwtID,
                                                           @Param("sub") String sub,
                                                           @Param("token_type") TokenType tokenType);
    @Modifying
    @Query("DELETE FROM ActiveResetToken a WHERE a.sub = :sub AND a.tokenType = :token_type")
    void deleteTokenBySub(@Param("sub") String sub, @Param("token_type") TokenType tokenType);


}
