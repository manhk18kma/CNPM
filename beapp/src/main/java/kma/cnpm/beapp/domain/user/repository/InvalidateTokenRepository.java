package kma.cnpm.beapp.domain.user.repository;

import kma.cnwat.be.domain.user.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InvalidateTokenRepository extends JpaRepository<InvalidatedToken , String> {


}
