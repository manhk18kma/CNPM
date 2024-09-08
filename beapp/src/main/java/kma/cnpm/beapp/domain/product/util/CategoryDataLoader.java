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
                new Category("Electronics", "All kinds of electronic items like phones, laptops, etc.", null),
                new Category("Books", "Books from all genres and subjects.", null),
                new Category("Clothing", "New and second-hand clothing.", null),
                new Category("Home Appliances", "Appliances and tools for the home.", null),
                new Category("Sports Equipment", "Sportswear, gear, and equipment.", null)
        );

        // Check if categories exist, and if not, save them
        for (Category category : defaultCategories) {
            if (!categoryRepository.existsByName(category.getName())) {
                categoryRepository.save(category);
            }
        }
    }
}
