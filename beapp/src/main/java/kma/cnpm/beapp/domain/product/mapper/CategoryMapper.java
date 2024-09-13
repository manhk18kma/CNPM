package kma.cnpm.beapp.domain.product.mapper;

import kma.cnpm.beapp.domain.product.dto.CategoryDto;
import kma.cnpm.beapp.domain.product.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto map(Category category);
    Category map(CategoryDto categoryDto);

}
