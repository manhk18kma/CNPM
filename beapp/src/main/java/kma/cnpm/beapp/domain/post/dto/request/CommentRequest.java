package kma.cnpm.beapp.domain.post.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    @NotNull(message = "Post ID cannot be null.")
    private Integer postId;

    @NotEmpty(message = "Content cannot be empty.")
    @Size(max = 500, message = "Content cannot exceed 500 characters.")
    private String content;

}
