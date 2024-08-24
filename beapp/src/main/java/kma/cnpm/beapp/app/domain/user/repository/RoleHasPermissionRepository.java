package kma.cnwat.be.domain.user.repository;

import kma.cnwat.be.domain.user.entity.RoleHasPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleHasPermissionRepository extends JpaRepository<RoleHasPermission, Long> {
}
