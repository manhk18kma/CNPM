package kma.cnpm.beapp.domain.product.repository;

import kma.cnpm.beapp.domain.product.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, Integer> {
}
