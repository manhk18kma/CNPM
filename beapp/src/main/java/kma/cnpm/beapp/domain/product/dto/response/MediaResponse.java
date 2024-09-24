package kma.cnpm.beapp.domain.product.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class MediaResponse {

    private Integer id;

    private String url;

    private String type; // "IMAGE" or "VIDEO"

    private Integer postId;

    public MediaResponse(Integer id, String url, String type, Integer postId) {
        this.id = id;
        this.url = url;
        this.type = type;
        this.postId = postId;
    }

}
