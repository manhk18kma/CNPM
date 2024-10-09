package kma.cnpm.beapp.domain.post.service.impl;

import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.common.notificationDto.PostApproved;
import kma.cnpm.beapp.domain.common.notificationDto.PostCreated;
import kma.cnpm.beapp.domain.common.notificationDto.PostRemoved;
import kma.cnpm.beapp.domain.notification.service.NotificationService;
import kma.cnpm.beapp.domain.post.dto.request.PostRequest;
import kma.cnpm.beapp.domain.post.dto.response.PostResponse;
import kma.cnpm.beapp.domain.post.entity.Post;
import kma.cnpm.beapp.domain.post.mapper.PostMapper;
import kma.cnpm.beapp.domain.post.repository.PostRepository;
import kma.cnpm.beapp.domain.post.service.CommentService;
import kma.cnpm.beapp.domain.post.service.LikeService;
import kma.cnpm.beapp.domain.post.service.PostService;
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

import static kma.cnpm.beapp.domain.common.exception.AppErrorCode.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {

    PostRepository postRepository;
    PostMapper postMapper;
    CommentService commentService;
    LikeService likeService;
    ProductService productService;
    AuthService authService;
    UserService userService;
    NotificationService notificationService;

    @Override
    public void createPost(PostRequest postRequest) {
        User user = userService.findUserById(authService.getAuthenticationName());
        Post post = postMapper.map(postRequest);
        post.setUserId(user.getId());
        post.setStatus("PENDING");
        post.setIsApproved(false);
        if (postRequest.getProductRequest() != null)
            post.setProductId(productService.save(postRequest.getProductRequest()).getId());
        postRepository.save(post);
        notificationService.postCreated(PostCreated.builder()
                .postId(Long.valueOf(post.getId()))
                .postUrlImg(null)
                .contentSnippet(post.getContent())
                .posterId(post.getUserId()).build());
    }

    @Override
    public void updatePost(Integer id, PostRequest postRequest) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new AppException(AppErrorCode.POST_NOT_EXISTED));

        User user = userService.findUserById(authService.getAuthenticationName());
        if (!user.getId().equals(post.getUserId()))
            throw new AppException(UNAUTHORIZED);

        post.setContent(postRequest.getContent());
        if (postRequest.getProductRequest() != null) {
            productService.deleteById(post.getProductId());
            post.setProductId(productService.save(postRequest.getProductRequest()).getId());
        }
        postRepository.save(post);
    }

    @Override
    public void deletePost(Integer id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new AppException(AppErrorCode.POST_NOT_EXISTED));
        User user = userService.findUserById(authService.getAuthenticationName());
        if (!user.getId().equals(post.getUserId()))
            throw new AppException(UNAUTHORIZED);
        productService.deleteById(post.getProductId());
        postRepository.deleteById(id);
        notificationService.postRemoved(PostRemoved.builder()
                .postId(Long.valueOf(post.getId())).build());
    }

    @Override
    public void approvePost(Integer id) {
        // role admin
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new AppException(AppErrorCode.POST_NOT_EXISTED));
        post.setIsApproved(true);
        post.setStatus("ACTION");
        postRepository.save(post);
        notificationService.postApproved(PostApproved.builder()
                .postId(Long.valueOf(post.getId()))
                .postUrlImg(null)
                .contentSnippet(post.getContent())
                .posterId(post.getUserId()).build());
    }

    @Override
    public void unApprovePost(Integer id) {
        // role admin
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new AppException(AppErrorCode.POST_NOT_EXISTED));
        post.setIsApproved(false);
        post.setStatus("INACTION");
        postRepository.save(post);
    }

    @Override
    public PostResponse getPostById(Integer id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new AppException(AppErrorCode.POST_NOT_EXISTED));
        PostResponse postResponse = postMapper.map(post);
        postResponse.setUserName(userService.findUserById(String.valueOf(post.getUserId())).getFullName());
        postResponse.setLiked(likeService.liked(post.getId()));
        postResponse.setCommentTotal(commentService.countComments(post.getId()));
        postResponse.setLikeTotal(likeService.countLikes(post.getId()));
        if (post.getProductId() != null)
            postResponse.setProductResponse(productService.getProductById(post.getProductId()));
        return postResponse;
    }

    @Override
    public PostResponse getPostByProductId(Integer productId) {
        Post post = postRepository.findByProductId(productId);
        PostResponse postResponse = postMapper.map(post);
        postResponse.setUserName(userService.findUserById(String.valueOf(post.getUserId())).getFullName());
        postResponse.setLiked(likeService.liked(post.getId()));
        postResponse.setCommentTotal(commentService.countComments(post.getId()));
        postResponse.setLikeTotal(likeService.countLikes(post.getId()));
        if (post.getProductId() != null)
            postResponse.setProductResponse(productService.getProductById(post.getProductId()));
        return postResponse;
    }

    @Override
    public List<PostResponse> findPostByTitle(String content) {
        List<Post> posts = postRepository.findByContentContaining(content);
        return posts.stream()
                .map(postMapper::map)
                .peek(postResponse -> {
                    postResponse.setUserName(userService.findUserById(String.valueOf(postResponse.getUserId())).getFullName());
                    postResponse.setLiked(likeService.liked(postResponse.getId()));
                    postResponse.setLikeTotal(likeService.countLikes(postResponse.getId()));
                    postResponse.setCommentTotal(commentService.countComments(postResponse.getId()));
                    if (postResponse.getProductResponse().getId() != null) {
                        postResponse.setProductResponse(
                                productService.getProductById(postResponse.getProductResponse().getId()));
                    }
                })
                .toList();
    }

    @Override
    public List<PostResponse> getPostsByUserId(Long userId) {
        List<Post> posts = postRepository.findByUserIdAndIsApprovedOrderByCreatedAtDesc(userId, true);
        return posts.stream()
                .map(postMapper::map)
                .peek(postResponse -> {
                    postResponse.setUserName(userService.findUserById(String.valueOf(postResponse.getUserId())).getFullName());
                    postResponse.setLiked(likeService.liked(postResponse.getId()));
                    postResponse.setLikeTotal(likeService.countLikes(postResponse.getId()));
                    postResponse.setCommentTotal(commentService.countComments(postResponse.getId()));
                    if (postResponse.getProductResponse().getId() != null) {
                        postResponse.setProductResponse(
                                productService.getProductById(postResponse.getProductResponse().getId()));
                    }
                })
                .toList();
    }

    @Override
    public List<PostResponse> getPostsByStatus(String status) {
        List<Post> posts = postRepository.findByStatusOrderByCreatedAtDesc(status);
        return posts.stream()
                .map(postMapper::map)
                .peek(postResponse -> {
                    postResponse.setUserName(userService.findUserById(String.valueOf(postResponse.getUserId())).getFullName());
                    postResponse.setLiked(likeService.liked(postResponse.getId()));
                    postResponse.setLikeTotal(likeService.countLikes(postResponse.getId()));
                    postResponse.setCommentTotal(commentService.countComments(postResponse.getId()));
                    if (postResponse.getProductResponse().getId() != null) {
                        postResponse.setProductResponse(
                                productService.getProductById(postResponse.getProductResponse().getId()));
                    }
                })
                .toList();
    }

    @Override
    public int countPostOfUser(long userId) {
        return postRepository.countPostOfUser(userId);
    }

    @Override
    public List<PostResponse> getPostsByApproved(Boolean isApproved) {
        List<Post> posts = postRepository.findByIsApprovedAndStatusOrderByCreatedAtDesc(false, "PENDING");
        return posts.stream()
                .map(postMapper::map)
                .peek(postResponse -> {
                    postResponse.setUserName(userService.findUserById(String.valueOf(postResponse.getUserId())).getFullName());
                    postResponse.setLiked(likeService.liked(postResponse.getId()));
                    postResponse.setLikeTotal(likeService.countLikes(postResponse.getId()));
                    postResponse.setCommentTotal(commentService.countComments(postResponse.getId()));
                    if (postResponse.getProductResponse().getId() != null) {
                        postResponse.setProductResponse(
                                productService.getProductById(postResponse.getProductResponse().getId()));
                    }
                })
                .toList();
    }

}
