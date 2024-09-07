package kma.cnpm.beapp.domain.product.service;

import kma.cnpm.beapp.domain.common.dto.ProductRequest;
import kma.cnpm.beapp.domain.product.dto.request.UploadFileRequest;
import kma.cnpm.beapp.domain.common.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse save(ProductRequest productRequest, UploadFileRequest uploadFileRequest);
    ProductResponse update(Integer id, ProductRequest productRequest);
    ProductResponse uploadFile(UploadFileRequest uploadFileRequest);
    void deleteById(Integer id);

    ProductResponse getProductById(Integer id);
    List<ProductResponse> getProductsByName(String name);
    List<ProductResponse> getProductsBySellerId(Long sellerId);
    List<ProductResponse> getProductsByCategory(Integer categoryId);

}
