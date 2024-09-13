package kma.cnpm.beapp.domain.common.dto;

import jakarta.validation.constraints.*;
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
public class ProductRequest {

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 100, message = "Tên sản phẩm phải ít hơn 100 ký tự")
    private String name;

    @Size(max = 500, message = "Mô tả phải ít hơn 500 ký tự")
    private String description;

    @NotNull(message = "Giá là bắt buộc")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải lớn hơn 0")
    private BigDecimal price;

    @NotNull(message = "Số lượng là bắt buộc")
    @Min(value = 1, message = "Số lượng phải ít nhất là 1")
    private Integer quantity;

    @NotNull(message = "ID danh mục là bắt buộc")
    private Integer categoryId;

    private List<String> imageBase64;

    private List<String> videoBase64;

}
