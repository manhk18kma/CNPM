package kma.cnpm.beapp.domain.post.service;

import kma.cnpm.beapp.domain.post.dto.response.PostResponse;

import java.util.List;

public interface LikeService {

    void likePost(Integer postId);
    void unlikePost(Integer postId);

    Boolean liked(Integer postId);
    Integer countLikes(Integer postId);
    List<PostResponse> getAllLikedPostsByUserId();

}
