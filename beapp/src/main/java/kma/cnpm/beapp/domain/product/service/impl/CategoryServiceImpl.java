package kma.cnpm.beapp.domain.product.service.impl;

import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.product.dto.CategoryDto;
import kma.cnpm.beapp.domain.product.entity.Category;
import kma.cnpm.beapp.domain.product.mapper.CategoryMapper;
import kma.cnpm.beapp.domain.product.repository.CategoryRepository;
import kma.cnpm.beapp.domain.product.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    @Override
    public CategoryDto save(CategoryDto categoryDto) {
        Category category = categoryRepository.save(categoryMapper.map(categoryDto));
        return categoryMapper.map(category);
    }

    @Override
    public void deleteById(Integer id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::map)
                .toList();
    }

    @Override
    public CategoryDto getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(AppErrorCode.CATEGORY_NOT_EXISTED));
        return categoryMapper.map(category);
    }
}
