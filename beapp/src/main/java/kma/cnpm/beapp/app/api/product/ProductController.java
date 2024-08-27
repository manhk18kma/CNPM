package kma.cnpm.beapp.app.api.product;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kma.cnpm.beapp.domain.common.dto.ResponseData;
import kma.cnpm.beapp.domain.product.dto.request.ProductRequest;
import kma.cnpm.beapp.domain.product.dto.response.ProductResponse;
import kma.cnpm.beapp.domain.product.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
                new Date(),
                productService.save(productRequest));
    }

    @PutMapping("/{id}")
    public ResponseData<ProductResponse> updateProduct(@PathVariable Integer id,
                                                       @RequestBody @Valid ProductRequest productRequest) {
        return new ResponseData<>(HttpStatus.CREATED.value(),
                "Product created successfully",
                new Date(),
                productService.update(id, productRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseData<String> deleteProduct(@PathVariable Integer id) {
        productService.deleteById(id);
        return new ResponseData<>(HttpStatus.CREATED.value(),
                "Product created successfully",
                new Date());
    }

    @GetMapping("/name/{name}")
    public ResponseData<List<ProductResponse>> getProductsByName(@PathVariable String name) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Get products by name successfully",
                new Date(),
                productService.getProductsByName(name));
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseData<List<ProductResponse>> getProductsBySellerId(@PathVariable Long sellerId) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Get products by seller successfully",
                new Date(),
                productService.getProductsBySellerId(sellerId));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseData<List<ProductResponse>> getProductsByCategory(@PathVariable Integer categoryId) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Get products by category successfully",
                new Date(),
                productService.getProductsByCategory(categoryId));
    }

}
