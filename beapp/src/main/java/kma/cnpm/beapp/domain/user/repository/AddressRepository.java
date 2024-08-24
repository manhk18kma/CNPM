package kma.cnpm.beapp.domain.user.repository;

import kma.cnwat.be.domain.user.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address , Long> {
}
