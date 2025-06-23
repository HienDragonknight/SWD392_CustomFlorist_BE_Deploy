package edu.fpt.customflorist.services.Category;

import edu.fpt.customflorist.dtos.Category.CategoryDTO;
import edu.fpt.customflorist.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO) throws Exception;
    Category updateCategory(Long categoryId, CategoryDTO categoryDTO) throws Exception;
    Category getCategoryById(Long categoryId) throws Exception;
    Page<Category> findAll(String keyword, Pageable pageable) throws Exception;
    Page<Category> findAllActive(String keyword, Pageable pageable) throws Exception;
}
