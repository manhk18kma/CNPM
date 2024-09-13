package kma.cnpm.beapp.domain.post.service;

import kma.cnpm.beapp.domain.post.dto.request.PostRequest;
import kma.cnpm.beapp.domain.post.dto.response.PostResponse;

import java.util.List;

public interface PostService {

    void createPost(PostRequest postRequest);
    void updatePost(Integer id, PostRequest postRequest);
    void deletePost(Integer id);

    PostResponse getPostById(Integer id);
    PostResponse getPostByProductId(Integer productId);
    List<PostResponse> findPostByTitle(String title);
    List<PostResponse> getPostsByUserId(Long userId);
    List<PostResponse> getPostsByStatus(String status);

    int countPostOfUser(long userId);
    List<PostResponse> getPostsByApproved(Boolean isApproved);


}
