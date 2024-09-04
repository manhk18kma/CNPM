package kma.cnpm.beapp.domain.post.service;

import kma.cnpm.beapp.domain.post.dto.request.CommentRequest;
import kma.cnpm.beapp.domain.post.dto.response.CommentResponse;

import java.util.List;

public interface CommentService {

    void addComment(CommentRequest commentRequest);
    void deleteComment(Long id);

    List<CommentResponse> getAllCommentByPostId(Integer postId);
    List<CommentResponse> getAllCommentByUserId(Long userId);

}
