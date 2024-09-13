package kma.cnpm.beapp.domain.product.repository;

import kma.cnpm.beapp.domain.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Boolean existsByName(String name);
}
