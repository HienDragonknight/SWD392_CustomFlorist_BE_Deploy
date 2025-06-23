package edu.fpt.customflorist.services.Category;

import edu.fpt.customflorist.dtos.Category.CategoryDTO;
import edu.fpt.customflorist.models.Category;
import edu.fpt.customflorist.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CategoryDTO categoryDTO) throws Exception {
        if (categoryRepository.findByNameContaining(categoryDTO.getName(), Pageable.unpaged()).hasContent()) {
            throw new Exception("Category with this name already exists");
        }

        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setIsActive(categoryDTO.getIsActive());

        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long categoryId, CategoryDTO categoryDTO) throws Exception {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new Exception("Category not found"));

        boolean nameExists = categoryRepository.existsByNameAndCategoryIdNot(categoryDTO.getName(), categoryId);
        if (nameExists) {
            throw new Exception("Category name already exists");
        }

        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setIsActive(categoryDTO.getIsActive());

        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long categoryId) throws Exception {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new Exception("Category not found"));
    }

    @Override
    public Page<Category> findAll(String keyword, Pageable pageable) {
        return categoryRepository.findByNameContaining(
                keyword != null ? keyword : "", pageable);
    }

    @Override
    public Page<Category> findAllActive(String keyword, Pageable pageable) {
        return categoryRepository.findByNameContainingAndIsActiveTrue(
                keyword != null ? keyword : "", pageable);
    }


}
