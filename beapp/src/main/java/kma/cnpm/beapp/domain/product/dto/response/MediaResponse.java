package kma.cnpm.beapp.domain.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaResponse {

    private Integer id;

    private String url;

    private String type; // "IMAGE" or "VIDEO"

}
