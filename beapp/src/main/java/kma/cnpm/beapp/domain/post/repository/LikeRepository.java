package kma.cnpm.beapp.domain.post.repository;

import kma.cnpm.beapp.domain.post.entity.Like;
import kma.cnpm.beapp.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Boolean existsByPostAndUserId(Post post, Long userId);
    Like findByPostAndUserId(Post post, Long userId);
    Integer countByPost(Post post);

    @Query("SELECT l.post FROM Like l WHERE l.userId = :userId")
    List<Post> findAllLikedPostsByUserId(@Param("userId") Long userId);
}
