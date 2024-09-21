package kma.cnpm.beapp.domain.post.repository;

import kma.cnpm.beapp.domain.post.entity.Like;
import kma.cnpm.beapp.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByPostAndUserId(Post post, Long userId);
    Integer countByPost(Post post);
}
