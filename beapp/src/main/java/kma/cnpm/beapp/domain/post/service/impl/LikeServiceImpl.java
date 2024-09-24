package kma.cnpm.beapp.domain.post.service.impl;

import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.post.dto.response.PostResponse;
import kma.cnpm.beapp.domain.post.entity.Like;
import kma.cnpm.beapp.domain.post.entity.Post;
import kma.cnpm.beapp.domain.post.mapper.PostMapper;
import kma.cnpm.beapp.domain.post.repository.LikeRepository;
import kma.cnpm.beapp.domain.post.repository.PostRepository;
import kma.cnpm.beapp.domain.post.service.CommentService;
import kma.cnpm.beapp.domain.post.service.LikeService;
import kma.cnpm.beapp.domain.product.service.ProductService;
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

import static kma.cnpm.beapp.domain.common.exception.AppErrorCode.LIKE_EXISTED;
import static kma.cnpm.beapp.domain.common.exception.AppErrorCode.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LikeServiceImpl implements LikeService {

    LikeRepository likeRepository;
    PostRepository postRepository;
    UserService userService;
    AuthService authService;
    CommentService commentService;
    ProductService productService;
    PostMapper postMapper;

    @Override
    public void likePost(Integer postId) {
        User user = userService.findUserById(authService.getAuthenticationName());
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(AppErrorCode.POST_NOT_EXISTED));
        if (likeRepository.existsByPostAndUserId(post, user.getId()))
            throw new AppException(LIKE_EXISTED);
        Like like = new Like();
        like.setPost(post);
        like.setUserId(user.getId());
        likeRepository.save(like);
    }

    @Override
    public void unlikePost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(AppErrorCode.POST_NOT_EXISTED));

        User user = userService.findUserById(authService.getAuthenticationName());
        if (!user.getId().equals(post.getUserId()))
            throw new AppException(UNAUTHORIZED);
        likeRepository.existsByPostAndUserId(post, user.getId());
        Like like = likeRepository.findByPostAndUserId(post, user.getId());
        likeRepository.delete(like);
    }

    @Override
    public Boolean liked(Integer postId) {
        User user = userService.findUserById(authService.getAuthenticationName());
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(AppErrorCode.POST_NOT_EXISTED));
        return likeRepository.existsByPostAndUserId(post, user.getId());
    }

    @Override
    public Integer countLikes(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(AppErrorCode.POST_NOT_EXISTED));
        return likeRepository.countByPost(post);
    }

    @Override
    public List<PostResponse> getAllLikedPostsByUserId() {
        User user = userService.findUserById(authService.getAuthenticationName());
        List<Post> posts = likeRepository.findAllLikedPostsByUserId(user.getId());
        return posts.stream()
                .map(postMapper::map)
                .peek(postResponse -> {
                    postResponse.setLiked(this.liked(postResponse.getId()));
                    postResponse.setLikeTotal(this.countLikes(postResponse.getId()));
                    postResponse.setCommentTotal(commentService.countComments(postResponse.getId()));
                    if (postResponse.getProductResponse().getId() != null) {
                        postResponse.setProductResponse(
                                productService.getProductById(postResponse.getProductResponse().getId()));
                    }
                })
                .toList();
    }


}
