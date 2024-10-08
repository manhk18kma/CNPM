package kma.cnpm.beapp.domain.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kma.cnpm.beapp.domain.product.dto.response.MediaResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private String categoryName;

    @JsonProperty("medias")
    private List<MediaResponse> mediaResponses;

    private Long sellerId;
    private String sellerName;

}
