package kma.cnpm.beapp.domain.user.repository;

import kma.cnpm.beapp.domain.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT r FROM Role r WHERE r.roleName = :roleName")
    Role findByRoleName(@Param("roleName") String roleName);

}
