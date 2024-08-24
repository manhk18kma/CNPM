package kma.cnwat.be.domain.user.repository;

import kma.cnwat.be.domain.user.entity.User;
import kma.cnwat.be.domain.user.entity.UserHasRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHasRoleRepository extends JpaRepository<UserHasRole, Long> {
}
