package kma.cnpm.beapp.domain.product.repository;

import kma.cnpm.beapp.domain.product.entity.Media;
import kma.cnpm.beapp.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Integer> {
    List<Media> findAllByType(String type);
}
