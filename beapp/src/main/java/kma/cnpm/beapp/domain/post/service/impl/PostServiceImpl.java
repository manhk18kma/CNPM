package kma.cnpm.beapp.domain.post.service.impl;

import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.post.dto.request.PostRequest;
import kma.cnpm.beapp.domain.post.dto.response.PostResponse;
import kma.cnpm.beapp.domain.post.entity.Post;
import kma.cnpm.beapp.domain.post.mapper.PostMapper;
import kma.cnpm.beapp.domain.post.repository.PostRepository;
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
    ProductService productService;
    AuthService authService;
    UserService userService;

    @Override
    public void createPost(PostRequest postRequest) {
        User user = userService.findUserById(authService.getAuthenticationName());
        Post post = postMapper.map(postRequest);
        post.setUserId(user.getId());
        post.setStatus("ACTIVE");
        post.setIsApproved(false);
        if (postRequest.getProductRequest() != null)
            post.setProductId(productService.save(postRequest.getProductRequest()).getId());

        postRepository.save(post);
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
//            post.setProductId(productService.save(postRequest.getProductRequest()).getId());
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
    }

    @Override
    public PostResponse getPostById(Integer id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new AppException(AppErrorCode.POST_NOT_EXISTED));
        PostResponse postResponse = postMapper.map(post);
        postResponse.setProductResponse(productService.getProductById(post.getProductId()));
        return postResponse;
    }

    @Override
    public PostResponse getPostByProductId(Integer productId) {
        Post post = postRepository.findByProductId(productId);
        PostResponse postResponse = postMapper.map(post);
        postResponse.setProductResponse(productService.getProductById(post.getProductId()));
        return postResponse;
    }

    @Override
    public List<PostResponse> findPostByTitle(String content) {
        List<Post> posts = postRepository.findByContentContaining(content);
        return posts.stream()
                .map(postMapper::map)
                .peek(postResponse -> postResponse.setProductResponse(
                        productService.getProductById(postResponse.getProductResponse().getId())))
                .toList();
    }

    @Override
    public List<PostResponse> getPostsByUserId(Long userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        return posts.stream()
                .map(postMapper::map)
                .peek(postResponse -> postResponse.setProductResponse(
                        productService.getProductById(postResponse.getProductResponse().getId())))
                .toList();
    }

    @Override
    public List<PostResponse> getPostsByStatus(String status) {
        List<Post> posts = postRepository.findByStatus(status);
        return posts.stream()
                .map(postMapper::map)
                .peek(postResponse -> postResponse.setProductResponse(
                        productService.getProductById(postResponse.getProductResponse().getId())))
                .toList();
    }

    @Override
    public int countPostOfUser(long userId) {
        return postRepository.countPostOfUser(userId);
    }

    @Override
    public List<PostResponse> getPostsByApproved(Boolean isApproved) {
        List<Post> posts = postRepository.findByIsApprovedOrderByUpdatedAt(isApproved);
        return posts.stream()
                .map(postMapper::map)
                .peek(postResponse -> postResponse.setProductResponse(
                        productService.getProductById(postResponse.getProductResponse().getId())))
                .toList();
    }

}
