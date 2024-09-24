package kma.cnpm.beapp.app.api.post;

import io.swagger.v3.oas.annotations.tags.Tag;
import kma.cnpm.beapp.domain.common.dto.ResponseData;
import kma.cnpm.beapp.domain.post.dto.response.PostResponse;
import kma.cnpm.beapp.domain.post.service.LikeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/likes")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Like Controller", description = "APIs for like management")
public class LikeController {

    LikeService likeService;

    @PostMapping("/post/{postId}")
    public ResponseData<String> likePost(@PathVariable Integer postId) {
        likeService.likePost(postId);
        return new ResponseData<>(HttpStatus.CREATED.value(),
                "Thích bài đăng thành công",
                LocalDateTime.now());
    }

    @DeleteMapping("/post/{postId}")
    public ResponseData<String> unlikePost(@PathVariable Integer postId) {
        likeService.unlikePost(postId);
        return new ResponseData<>(HttpStatus.OK.value(),
                "Bỏ thích bài đăng thành công",
                LocalDateTime.now());
    }

    @GetMapping("/post/{postId}")
    public ResponseData<Integer> countLikes(@PathVariable Integer postId) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Hiển thị tổng số lượng thích bài đăng thành công",
                LocalDateTime.now(),
                likeService.countLikes(postId));
    }

    @GetMapping("/post")
    public ResponseData<List<PostResponse>> getAllLikedPosts() {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Hiển thị tất cả bài đăng đã thích thành công",
                LocalDateTime.now(),
                likeService.getAllLikedPostsByUserId());
    }

}
