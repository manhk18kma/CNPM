package kma.cnpm.beapp.domain.common.notificationDto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class CommentRemoved {
    Long postId;              // ID của bài viết
    Long posterId;            // ID của người đăng bài viết
    Long lastCommenterId;         // ID của người vừa bình luận(ngoại trừ co
    List<Long> otherCommentersId; // Danh sách ID của những người khác đã bình luận lasestCommenterId)
    String lastCommentSnippet;    // Phần trích nội dung của bình luận (có thể là tối đa 15 ký tự) // comment content
    String postUrlImg;        // URL hình ảnh đại diện của bài viết


    //    String contentSnippet = postRequest.getContent().length() > 15
//            ? postRequest.getContent().substring(0, 15)
//            : postRequest.getContent();
}
