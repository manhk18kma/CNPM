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

    @NotNull(message = "ID bài viết không được để trống.")
    private Integer postId;

    @NotEmpty(message = "Nội dung không được để trống.")
    @Size(max = 500, message = "Nội dung không được vượt quá 500 ký tự.")
    private String content;

}
