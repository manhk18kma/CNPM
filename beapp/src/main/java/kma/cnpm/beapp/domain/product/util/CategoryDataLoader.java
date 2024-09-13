package kma.cnpm.beapp.domain.product.util;

import kma.cnpm.beapp.domain.product.entity.Category;
import kma.cnpm.beapp.domain.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryDataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Category> defaultCategories = List.of(
                new Category("Điện tử", "Tất cả các loại thiết bị điện tử như điện thoại, máy tính xách tay, v.v.", null),
                new Category("Sách", "Sách thuộc mọi thể loại và chủ đề.", null),
                new Category("Quần áo", "Quần áo mới và đã qua sử dụng.", null),
                new Category("Đồ gia dụng", "Đồ dùng và dụng cụ cho gia đình.", null),
                new Category("Thiết bị thể thao", "Quần áo thể thao, dụng cụ và thiết bị.", null)
        );

        // Check if categories exist, and if not, save them
        for (Category category : defaultCategories) {
            if (!categoryRepository.existsByName(category.getName())) {
                categoryRepository.save(category);
            }
        }
    }
}
