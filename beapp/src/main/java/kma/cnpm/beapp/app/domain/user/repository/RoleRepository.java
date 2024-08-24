package kma.cnwat.be.domain.user.repository;

import kma.cnwat.be.domain.user.entity.Role;
import kma.cnwat.be.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
