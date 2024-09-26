package kma.cnpm.beapp.domain.common.notificationDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostCreated {
    Long postId;
    Long posterId;
    String contentSnippet; //post content
    String postUrlImg;

    //    String contentSnippet = postRequest.getContent().length() > 15
//            ? postRequest.getContent().substring(0, 15)
//            : postRequest.getContent();


}
