package kma.cnpm.beapp.domain.product.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileRequest {

    private Integer productId;

    @NotNull(message = "Ảnh không được để trống.")
    private List<MultipartFile> images;

    private List<MultipartFile> videos;
}
