package kma.cnpm.beapp.domain.product.service;

import kma.cnpm.beapp.domain.product.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto save(CategoryDto categoryDto);
    void deleteById(Integer id);

    List<CategoryDto> getAllCategories();
    CategoryDto getCategoryById(Integer id);

}
