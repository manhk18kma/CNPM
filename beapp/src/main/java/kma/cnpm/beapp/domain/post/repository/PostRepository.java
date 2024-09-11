package kma.cnpm.beapp.domain.post.repository;

import kma.cnpm.beapp.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    Post findByProductId(Integer productId);
    List<Post> findByTitleContaining(String title);
    List<Post> findByUserId(Long userId);
    List<Post> findByStatus(String status);

    @Query("SELECT COUNT(p.id) FROM Post p WHERE p.userId = :userId")
    int countPostOfUser(@Param("userId") long userId);
}
