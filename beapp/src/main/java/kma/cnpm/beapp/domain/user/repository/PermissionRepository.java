package kma.cnpm.beapp.domain.user.repository;

import kma.cnpm.beapp.domain.user.entity.Permission;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
