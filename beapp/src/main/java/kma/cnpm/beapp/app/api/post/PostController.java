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
import java.util.Date;
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
                "Post created successfully",
                LocalDateTime.now());
    }

    @PutMapping("/{id}")
    public ResponseData<String> updatePost(@PathVariable Integer id,
                                           @RequestBody @Valid PostRequest postRequest) {
        postService.updatePost(id, postRequest);
        return new ResponseData<>(HttpStatus.OK.value(),
                "Post updated successfully",
                LocalDateTime.now());
    }

    @DeleteMapping("/{id}")
    public ResponseData<String> deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
        return new ResponseData<>(HttpStatus.OK.value(),
                "Post deleted successfully",
                LocalDateTime.now());
    }

    @GetMapping("/{id}")
    public ResponseData<PostResponse> getPostsById(@PathVariable Integer id) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Get posts by id successfully",
                LocalDateTime.now(),
                postService.getPostById(id));
    }

    @GetMapping("/product/{productId}")
    public ResponseData<PostResponse> getPostsByProductId(@PathVariable Integer productId) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Get posts by id successfully",
                LocalDateTime.now(),
                postService.getPostByProductId(productId));
    }

    @GetMapping("/search/{title}")
    public ResponseData<List<PostResponse>> findPostsByTitle(@PathVariable String title) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Find posts by title successfully",
                LocalDateTime.now(),
                postService.findPostByTitle(title));
    }

    @GetMapping("/user/{userId}")
    public ResponseData<List<PostResponse>> getPostByUserId(@PathVariable Long userId) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "get post by user ID successfully",
                LocalDateTime.now(),
                postService.getPostsByUserId(userId));
    }

    @GetMapping("/status/{status}")
    public ResponseData<List<PostResponse>> getPostByStatus(@PathVariable String status) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "get post by status successfully",
                LocalDateTime.now(),
                postService.getPostsByStatus(status));
    }

    @GetMapping("/approve/{approve}")
    public ResponseData<List<PostResponse>> getPostByIsApproved(@PathVariable Boolean approve) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "get post by approve successfully",
                LocalDateTime.now(),
                postService.getPostsByApproved(approve));
    }

}
