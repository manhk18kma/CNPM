package kma.cnpm.beapp.domain.product.repository;

import kma.cnpm.beapp.domain.product.entity.Category;
import kma.cnpm.beapp.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByNameContaining(String name);
    List<Product> findBySellerId(Long sellerId);
    List<Product> findByCategory(Category category);

//    int countSoldProductOfUser(Long userId);
}
