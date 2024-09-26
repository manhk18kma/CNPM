package kma.cnpm.beapp.domain.common.notificationDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikeCreated {
    Long postId;
    Long posterId;
    Long likerId;
    String contentSnippet; //post content
    String postUrlImg;
    int countLikes;

    //    String contentSnippet = postRequest.getContent().length() > 15
//            ? postRequest.getContent().substring(0, 15)
//            : postRequest.getContent();


}
