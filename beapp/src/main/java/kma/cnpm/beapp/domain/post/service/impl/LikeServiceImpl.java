package kma.cnpm.beapp.domain.post.service.impl;

import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.post.entity.Like;
import kma.cnpm.beapp.domain.post.entity.Post;
import kma.cnpm.beapp.domain.post.repository.LikeRepository;
import kma.cnpm.beapp.domain.post.repository.PostRepository;
import kma.cnpm.beapp.domain.post.service.LikeService;
import kma.cnpm.beapp.domain.user.entity.User;
import kma.cnpm.beapp.domain.user.service.AuthService;
import kma.cnpm.beapp.domain.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
