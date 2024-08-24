package kma.cnwat.be.domain.user.repository;

import kma.cnwat.be.domain.user.entity.Permission;
import kma.cnwat.be.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
