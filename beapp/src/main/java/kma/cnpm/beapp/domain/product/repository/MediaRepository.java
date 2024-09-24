package kma.cnpm.beapp.domain.product.repository;

import kma.cnpm.beapp.domain.product.entity.Media;
import kma.cnpm.beapp.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Integer> {

    List<Media> findAllByType(String type);
    @Query("SELECT m FROM Media m WHERE m.product.sellerId = :sellerId")
    List<Media> findAllBySellerId(@Param("sellerId") Long sellerId);

    @Query("SELECT p.id FROM Post p JOIN Media m ON p.productId = m.product.id WHERE m.id = :mediaId")
    Integer findPostIdByMediaId(@Param("mediaId") Integer mediaId);


}
