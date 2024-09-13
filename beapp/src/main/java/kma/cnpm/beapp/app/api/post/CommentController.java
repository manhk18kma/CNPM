package kma.cnpm.beapp.app.api.post;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kma.cnpm.beapp.domain.common.dto.ResponseData;
import kma.cnpm.beapp.domain.post.dto.request.CommentRequest;
import kma.cnpm.beapp.domain.post.dto.response.CommentResponse;
import kma.cnpm.beapp.domain.post.service.CommentService;
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
@RequestMapping("/api/v1/comments")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Comment Controller", description = "APIs for comment management")
public class CommentController {

    CommentService commentService;

    @PostMapping
    public ResponseData<String> addComment(@RequestBody @Valid CommentRequest commentRequest) {
        commentService.addComment(commentRequest);
        return new ResponseData<>(HttpStatus.CREATED.value(),
                "Comment added successfully",
                LocalDateTime.now());
    }

    @DeleteMapping("/{id}")
    public ResponseData<String> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return new ResponseData<>(HttpStatus.OK.value(),
                "comment deleted successfully",
                LocalDateTime.now());
    }

    @GetMapping("/post/{postId}")
    public ResponseData<List<CommentResponse>> getAllCommentByPostId(@PathVariable Integer postId) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Comments get successfully",
                LocalDateTime.now(),
                commentService.getAllCommentByPostId(postId));
    }

    @GetMapping("/user/{userId}")
    public ResponseData<List<CommentResponse>> getAllCommentByUserId(@PathVariable Long userId) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Comments get successfully",
                LocalDateTime.now(),
                commentService.getAllCommentByUserId(userId));
    }

}
