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

    private String title;
    private String description;
    private String status;
    private Long userId;
    private Date createdAt;
    private Date updatedAt;

    @JsonProperty("product")
    private ProductResponse productResponse;

}
