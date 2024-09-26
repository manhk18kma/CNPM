package kma.cnpm.beapp.domain.common.notificationDto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CommentCreated {
    Long postId;              // ID của bài viết
    Long posterId;            // ID của người đăng bài viết
    Long commenterId;         // ID của người vừa bình luận
    List<Long> otherCommentersId; // Danh sách ID của những người khác đã bình luận (ngoại trừ commenterId)
    String commentSnippet;    // Phần trích nội dung của bình luận (có thể là tối đa 15 ký tự) // comment content
    String postUrlImg;        // URL hình ảnh đại diện của bài viết


    //    String contentSnippet = postRequest.getContent().length() > 15
//            ? postRequest.getContent().substring(0, 15)
//            : postRequest.getContent();
}
