package kma.cnpm.beapp.domain.post.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import kma.cnpm.beapp.domain.common.dto.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    private Integer id;
    private String content;
    private String status;
    private Boolean isApproved;
    private Long userId;
    private Boolean liked;
    private Integer commentTotal;
    private Integer likeTotal;
    private Date createdAt;
    private Date updatedAt;

    @JsonProperty("product")
    private ProductResponse productResponse;

}
