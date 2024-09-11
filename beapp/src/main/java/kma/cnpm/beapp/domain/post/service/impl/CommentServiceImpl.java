package kma.cnpm.beapp.domain.post.service.impl;

import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.post.dto.request.CommentRequest;
import kma.cnpm.beapp.domain.post.dto.response.CommentResponse;
import kma.cnpm.beapp.domain.post.entity.Comment;
import kma.cnpm.beapp.domain.post.entity.Post;
import kma.cnpm.beapp.domain.post.mapper.CommentMapper;
import kma.cnpm.beapp.domain.post.repository.CommentRepository;
import kma.cnpm.beapp.domain.post.repository.PostRepository;
import kma.cnpm.beapp.domain.post.service.CommentService;
import kma.cnpm.beapp.domain.user.entity.User;
import kma.cnpm.beapp.domain.user.service.AuthService;
import kma.cnpm.beapp.domain.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kma.cnpm.beapp.domain.common.exception.AppErrorCode.COMMENT_NOT_EXISTED;
import static kma.cnpm.beapp.domain.common.exception.AppErrorCode.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImpl implements CommentService {

    CommentRepository commentRepository;
    PostRepository postRepository;
    CommentMapper commentMapper;
    AuthService authService;
    UserService userService;


    @Override
    public void addComment(CommentRequest commentRequest) {
        Post post = postRepository.findById(commentRequest.getPostId())
                .orElseThrow(() -> new AppException(AppErrorCode.POST_NOT_EXISTED));
        User user = userService.findUserById(authService.getAuthenticationName());
        Comment comment = commentMapper.map(commentRequest);
        comment.setUserId(user.getId());
        comment.setPost(post);
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new AppException(COMMENT_NOT_EXISTED));
        User user = userService.findUserById(authService.getAuthenticationName());
        if (!user.getId().equals(comment.getUserId()))
            throw new AppException(UNAUTHORIZED);
        commentRepository.deleteById(id);
    }

    @Override
    public List<CommentResponse> getAllCommentByPostId(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(AppErrorCode.POST_NOT_EXISTED));
        List<Comment> comments = commentRepository.findByPost(post);
        return comments.stream()
                .map(commentMapper::map)
                .peek(commentResponse -> commentResponse.setUserName(
                        userService.findUserById(String.valueOf(commentResponse.getUserId())).getFullName()
                ))
                .toList();
    }

    @Override
    public List<CommentResponse> getAllCommentByUserId(Long userId) {
        List<Comment> comments = commentRepository.findByUserId(userId);
        return comments.stream()
                .map(commentMapper::map)
                .peek(commentResponse -> commentResponse.setUserName(
                        userService.findUserById(String.valueOf(commentResponse.getUserId())).getFullName()
                ))
                .toList();
    }
}
