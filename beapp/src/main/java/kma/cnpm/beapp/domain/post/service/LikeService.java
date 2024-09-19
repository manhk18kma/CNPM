package kma.cnpm.beapp.domain.post.service;

public interface LikeService {

    void likePost(Integer postId);
    void unlikePost(Integer postId);

    Integer countLikes(Integer postId);

}
