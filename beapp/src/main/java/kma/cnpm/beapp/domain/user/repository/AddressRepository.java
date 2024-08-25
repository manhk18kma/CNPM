package kma.cnpm.beapp.domain.user.repository;

import kma.cnpm.beapp.domain.user.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
