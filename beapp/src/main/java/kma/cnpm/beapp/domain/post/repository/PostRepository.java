package kma.cnpm.beapp.domain.post.repository;

import kma.cnpm.beapp.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    Post findByProductId(Integer productId);
    List<Post> findByContentContaining(String title);
    List<Post> findByUserIdAndIsApprovedOrderByCreatedAtDesc(Long userId, Boolean isApproved);
    List<Post> findByStatusOrderByCreatedAtDesc(String status);
    List<Post> findByIsApprovedOrderByCreatedAtDesc(Boolean isApproved);

    @Query("SELECT COUNT(p.id) FROM Post p WHERE p.userId = :userId")
    int countPostOfUser(@Param("userId") long userId);
}
