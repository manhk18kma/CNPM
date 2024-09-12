package kma.cnpm.beapp.app.api.product;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kma.cnpm.beapp.domain.common.dto.ProductRequest;
import kma.cnpm.beapp.domain.common.dto.ProductResponse;
import kma.cnpm.beapp.domain.common.dto.ResponseData;
import kma.cnpm.beapp.domain.product.dto.request.UploadFileRequest;
import kma.cnpm.beapp.domain.product.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Product Controller", description = "APIs for product management")
public class ProductController {

    ProductService productService;

    @PostMapping
    public ResponseData<ProductResponse> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        return new ResponseData<>(HttpStatus.CREATED.value(),
                "Product created successfully",
                LocalDateTime.now(),
                productService.save(productRequest));
    }

    @PutMapping("/{id}")
    public ResponseData<ProductResponse> updateProduct(@PathVariable Integer id,
                                                       @RequestBody @Valid ProductRequest productRequest) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Product created successfully",
                LocalDateTime.now(),
                productService.update(id, productRequest));
    }

    @PatchMapping
    public ResponseData<ProductResponse> uploadMediaProduct(@ModelAttribute @Valid UploadFileRequest uploadFileRequest) {
        return new ResponseData<>(HttpStatus.CREATED.value(),
                "Product uploaded successfully",
                LocalDateTime.now(),
                productService.uploadFile(uploadFileRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseData<String> deleteProduct(@PathVariable Integer id) {
        productService.deleteById(id);
        return new ResponseData<>(HttpStatus.OK.value(),
                "Product created successfully",
                LocalDateTime.now()
        );
    }

    @GetMapping("/name/{name}")
    public ResponseData<List<ProductResponse>> getProductsByName(@PathVariable String name) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Get products by name successfully",
                LocalDateTime.now(),
                productService.getProductsByName(name));
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseData<List<ProductResponse>> getProductsBySellerId(@PathVariable Long sellerId) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Get products by seller successfully",
                LocalDateTime.now(),
                productService.getProductsBySellerId(sellerId));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseData<List<ProductResponse>> getProductsByCategory(@PathVariable Integer categoryId) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Get products by category successfully",
                LocalDateTime.now(),
                productService.getProductsByCategory(categoryId));
    }

}
