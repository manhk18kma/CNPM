package kma.cnpm.beapp.app.api.post;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kma.cnpm.beapp.domain.common.dto.ResponseData;
import kma.cnpm.beapp.domain.post.dto.request.PostRequest;
import kma.cnpm.beapp.domain.post.dto.response.PostResponse;
import kma.cnpm.beapp.domain.post.service.PostService;
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
@RequestMapping("/api/v1/posts")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Post Controller", description = "APIs for post management")
public class PostController {

    PostService postService;

    @PostMapping
    public ResponseData<String> createPost(@RequestBody @Valid PostRequest postRequest) {
        postService.createPost(postRequest);
        return new ResponseData<>(HttpStatus.CREATED.value(),
                "Tạo bài đăng thành công",
                LocalDateTime.now());
    }

    @PutMapping("/{id}")
    public ResponseData<String> updatePost(@PathVariable Integer id,
                                           @RequestBody @Valid PostRequest postRequest) {
        postService.updatePost(id, postRequest);
        return new ResponseData<>(HttpStatus.OK.value(),
                "Chỉnh sửa bài đăng thành công",
                LocalDateTime.now());
    }

    @DeleteMapping("/{id}")
    public ResponseData<String> deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
        return new ResponseData<>(HttpStatus.OK.value(),
                "Xóa bài đăng thành công",
                LocalDateTime.now());
    }

    @PatchMapping("/approve/{id}")
    public ResponseData<String> approvePost(@PathVariable Integer id) {
        postService.approvePost(id);
        return new ResponseData<>(HttpStatus.OK.value(),
                "Duyệt bài đăng thành công",
                LocalDateTime.now());
    }

    @PatchMapping("/unApprove/{id}")
    public ResponseData<String> unApprovePost(@PathVariable Integer id) {
        postService.unApprovePost(id);
        return new ResponseData<>(HttpStatus.OK.value(),
                "Hủy Duyệt bài đăng thành công",
                LocalDateTime.now());
    }

    @GetMapping("/{id}")
    public ResponseData<PostResponse> getPostsById(@PathVariable Integer id) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Hiển thị bài đăng bằng ID thành công",
                LocalDateTime.now(),
                postService.getPostById(id));
    }

    @GetMapping("/product/{productId}")
    public ResponseData<PostResponse> getPostsByProductId(@PathVariable Integer productId) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Hiển thị bài đăng bằng ID sản phẩm thành công",
                LocalDateTime.now(),
                postService.getPostByProductId(productId));
    }

    @GetMapping("/search/{title}")
    public ResponseData<List<PostResponse>> findPostsByTitle(@PathVariable String title) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Tìm kiếm bài đăng bằng nội dung thành công",
                LocalDateTime.now(),
                postService.findPostByTitle(title));
    }

    @GetMapping("/user/{userId}")
    public ResponseData<List<PostResponse>> getPostByUserId(@PathVariable Long userId) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Hiển thị bài đăng bằng ID người dùng thành công",
                LocalDateTime.now(),
                postService.getPostsByUserId(userId));
    }

    @GetMapping("/status/{status}")
    public ResponseData<List<PostResponse>> getPostByStatus(@PathVariable String status) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Hiển thị bài đăng bằng trạng thái thành công",
                LocalDateTime.now(),
                postService.getPostsByStatus(status));
    }

    @GetMapping("/approve/{approve}")
    public ResponseData<List<PostResponse>> getPostByIsApproved(@PathVariable Boolean approve) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Hiển thị bài đăng bằng trạng thái duyệt thành công",
                LocalDateTime.now(),
                postService.getPostsByApproved(approve));
    }

}
