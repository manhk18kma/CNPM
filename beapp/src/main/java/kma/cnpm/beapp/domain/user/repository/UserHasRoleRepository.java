package kma.cnpm.beapp.domain.user.repository;


import kma.cnpm.beapp.domain.user.entity.UserHasRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHasRoleRepository extends JpaRepository<UserHasRole, Long> {
}
