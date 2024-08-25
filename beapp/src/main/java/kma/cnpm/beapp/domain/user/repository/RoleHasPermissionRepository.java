package kma.cnpm.beapp.domain.user.repository;

import kma.cnpm.beapp.domain.user.entity.RoleHasPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleHasPermissionRepository extends JpaRepository<RoleHasPermission, Long> {
}
