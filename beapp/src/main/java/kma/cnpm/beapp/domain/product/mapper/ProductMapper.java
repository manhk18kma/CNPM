package kma.cnpm.beapp.domain.product.mapper;

import kma.cnpm.beapp.domain.product.dto.request.ProductRequest;
import kma.cnpm.beapp.domain.product.dto.response.ProductResponse;
import kma.cnpm.beapp.domain.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categoryName", source = "product.category.name")
    @Mapping(target = "sellerName", source = "sellerName")
    ProductResponse map(Product product, String sellerName);

    @Mapping(target = "category.id", source = "categoryId")
    Product map(ProductRequest productRequest);

    @Mapping(target = "categoryName", source = "category.name")
    ProductResponse map(Product product);

}
