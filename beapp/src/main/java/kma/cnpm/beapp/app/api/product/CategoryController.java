package kma.cnpm.beapp.app.api.product;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kma.cnpm.beapp.domain.common.dto.ResponseData;
import kma.cnpm.beapp.domain.product.dto.CategoryDto;
import kma.cnpm.beapp.domain.product.service.CategoryService;
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
@RequestMapping("/api/v1/categories")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Category Controller", description = "APIs for category management")
public class CategoryController {

    CategoryService categoryService;

    @PostMapping
    public ResponseData<CategoryDto> createCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return new ResponseData<>(HttpStatus.CREATED.value(),
                "Category created successfully",
                new Date(),
                categoryService.save(categoryDto));
    }

    @DeleteMapping("/{id}")
    public ResponseData<String> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteById(id);
        return new ResponseData<>(HttpStatus.OK.value(),
                "Category deleted successfully",
                new Date());
    }

    @GetMapping
    public ResponseData<List<CategoryDto>> getAllCategories() {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Categories find successfully",
                new Date(),
                categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseData<CategoryDto> getCategoryById(@PathVariable Integer id) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Category find successfully",
                new Date(),
                categoryService.getCategoryById(id));
    }

}
