package kma.cnpm.beapp.domain.post.service;

import kma.cnpm.beapp.domain.post.entity.Post;

public interface LikeService {

    void likePost(Integer postId);
    void unlikePost(Integer postId);

    Boolean liked(Integer postId);
    Integer countLikes(Integer postId);

}
