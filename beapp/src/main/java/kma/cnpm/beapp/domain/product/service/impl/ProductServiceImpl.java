package kma.cnpm.beapp.domain.product.service.impl;

import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.common.upload.ImageService;
import kma.cnpm.beapp.domain.common.dto.ProductRequest;
import kma.cnpm.beapp.domain.product.dto.request.UploadFileRequest;
import kma.cnpm.beapp.domain.common.dto.ProductResponse;
import kma.cnpm.beapp.domain.product.entity.Category;
import kma.cnpm.beapp.domain.product.entity.Media;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static kma.cnpm.beapp.domain.common.exception.AppErrorCode.*;

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
    ImageService imageService;

    @Override
    public ProductResponse save(ProductRequest productRequest, UploadFileRequest uploadFileRequest) {
        Product product = productMapper.map(productRequest);
        //set seller id by authService
        User user = userService.findUserById(authService.getAuthenticationName());
        product.setSellerId(user.getId());
        List<Media> mediaList = new ArrayList<>();
        if (uploadFileRequest.getImages() != null) {
            for (MultipartFile image : uploadFileRequest.getImages()) {
                String imageUrl = imageService.uploadImage(image, UUID.randomUUID().toString());
                mediaList.add(Media.builder()
                        .product(product)
                        .url(imageUrl)
                        .type("IMAGE")
                        .build());
            }
        }
        if (uploadFileRequest.getVideos() != null) {
            for (MultipartFile video : uploadFileRequest.getVideos()) {
                String videoUrl = imageService.uploadVideo(video, UUID.randomUUID().toString());
                mediaList.add(Media.builder()
                        .product(product)
                        .url(videoUrl)
                        .type("VIDEO")
                        .build());
            }
        }
        product.setMedias(mediaList);
        productRepository.save(product);
        return productMapper.map(product, user.getFullName());
    }


    @Override
    public ProductResponse update(Integer id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(AppErrorCode.PRODUCT_NOT_EXISTED));

        User user = userService.findUserById(authService.getAuthenticationName());
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
        return productMapper.map(product, user.getFullName());
    }

    @Override
    public ProductResponse uploadFile(UploadFileRequest uploadFileRequest) {
        Product product = productRepository.findById(uploadFileRequest.getProductId())
                .orElseThrow(() -> new AppException(AppErrorCode.PRODUCT_NOT_EXISTED));

        User user = userService.findUserById(authService.getAuthenticationName());
        if (!user.getId().equals(product.getSellerId()))
            throw new AppException(UNAUTHORIZED);
        List<Media> mediaList = new ArrayList<>();
        if (uploadFileRequest.getImages() != null) {
            for (MultipartFile image : uploadFileRequest.getImages()) {
                String imageUrl = imageService.uploadImage(image, UUID.randomUUID().toString());
                mediaList.add(Media.builder()
                        .product(product)
                        .url(imageUrl)
                        .type("IMAGE")
                        .build());
            }
        }
        if (uploadFileRequest.getVideos() != null) {
            for (MultipartFile video : uploadFileRequest.getVideos()) {
                String videoUrl = imageService.uploadVideo(video, UUID.randomUUID().toString());
                mediaList.add(Media.builder()
                        .product(product)
                        .url(videoUrl)
                        .type("VIDEO")
                        .build());
            }
        }
        product.setMedias(mediaList);
        productRepository.save(product);
        return productMapper.map(product, user.getFullName());
    }

    @Override
    public void deleteById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(AppErrorCode.PRODUCT_NOT_EXISTED));

        User user = userService.findUserById(authService.getAuthenticationName());
        if (!user.getId().equals(product.getSellerId()))
            throw new AppException(UNAUTHORIZED);
        productRepository.deleteById(id);
    }

    @Override
    public ProductResponse getProductById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(AppErrorCode.PRODUCT_NOT_EXISTED));
        ProductResponse productResponse = productMapper.map(product);
        productResponse.setSellerName(userService.findUserById(String.valueOf(productResponse.getSellerId())).getFullName());
        return productResponse;
    }

    @Override
    public List<ProductResponse> getProductsByName(String name) {
        List<Product> products = productRepository.findByNameContaining(name);
        return products.stream()
                .map(productMapper::map)
                .peek(productResponse -> productResponse.setSellerName(
                        userService.findUserById(String.valueOf(productResponse.getSellerId())).getFullName()))
                .toList();
    }

    @Override
    public List<ProductResponse> getProductsBySellerId(Long sellerId) {
        List<Product> products = productRepository.findBySellerId(sellerId);
        return products.stream()
                .map(productMapper::map)
                .peek(productResponse -> productResponse.setSellerName(
                        userService.findUserById(String.valueOf(productResponse.getSellerId())).getFullName()))
                .toList();
    }

    @Override
    public List<ProductResponse> getProductsByCategory(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(CATEGORY_NOT_EXISTED));
        List<Product> products = productRepository.findByCategory(category);
        return products.stream()
                .map(productMapper::map)
                .peek(productResponse -> productResponse.setSellerName(
                        userService.findUserById(String.valueOf(productResponse.getSellerId())).getFullName()))
                .toList();
    }

//    Refactor this method
    @Override
    public int countSoldProductOfUser(Long userId) {
        return 10;
//        return productRepository.countSoldProductOfUser(userId);
    }
}
