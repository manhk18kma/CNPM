package kma.cnpm.beapp.domain.product.service.impl;

import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.product.dto.request.ProductRequest;
import kma.cnpm.beapp.domain.product.dto.response.ProductResponse;
import kma.cnpm.beapp.domain.product.entity.Category;
import kma.cnpm.beapp.domain.product.entity.Product;
import kma.cnpm.beapp.domain.product.mapper.ProductMapper;
import kma.cnpm.beapp.domain.product.repository.CategoryRepository;
import kma.cnpm.beapp.domain.product.repository.ProductRepository;
import kma.cnpm.beapp.domain.product.service.ProductService;
import kma.cnpm.beapp.domain.user.entity.User;
import kma.cnpm.beapp.domain.user.service.AuthService;
import kma.cnpm.beapp.domain.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kma.cnpm.beapp.domain.common.exception.AppErrorCode.CATEGORY_NOT_EXISTED;
import static kma.cnpm.beapp.domain.common.exception.AppErrorCode.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductMapper productMapper;
    AuthService authService;
    UserService userService;

    @Override
    public ProductResponse save(ProductRequest productRequest) {
        Product product = productMapper.map(productRequest);
        //set seller id by authService
        User user = userService.findUserByUsername(authService.getAuthenticationName());
        product.setSellerId(user.getId());
        productRepository.save(product);
        return productMapper.map(product, user.getUsername());
    }

    @Override
    public ProductResponse update(Integer id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(AppErrorCode.PRODUCT_NOT_EXISTED));

        User user = userService.findUserByUsername(authService.getAuthenticationName());
        if (!user.getId().equals(product.getSellerId()))
            throw new AppException(UNAUTHORIZED);

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                        .orElseThrow(() -> new AppException(AppErrorCode.CATEGORY_NOT_EXISTED));
        product.setCategory(category);
        productRepository.save(product);
        return productMapper.map(product, user.getUsername());
    }

    @Override
    public void deleteById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(AppErrorCode.PRODUCT_NOT_EXISTED));

        User user = userService.findUserByUsername(authService.getAuthenticationName());
        if (!user.getId().equals(product.getSellerId()))
            throw new AppException(UNAUTHORIZED);
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductResponse> getProductsByName(String name) {
        List<Product> products = productRepository.findByNameContaining(name);
        return products.stream()
                .map(productMapper::map)
                .toList();
    }

    @Override
    public List<ProductResponse> getProductsBySellerId(Long sellerId) {
        List<Product> products = productRepository.findBySellerId(sellerId);
        return products.stream()
                .map(productMapper::map)
                .toList();
    }

    @Override
    public List<ProductResponse> getProductsByCategory(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(CATEGORY_NOT_EXISTED));
        List<Product> products = productRepository.findByCategory(category);
        return products.stream()
                .map(productMapper::map)
                .toList();
    }
}
