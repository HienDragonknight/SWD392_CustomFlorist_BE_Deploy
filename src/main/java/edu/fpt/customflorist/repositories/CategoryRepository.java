package edu.fpt.customflorist.repositories;

import edu.fpt.customflorist.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findByNameContaining(String keyword, Pageable pageable);
    Page<Category> findByNameContainingAndIsActiveTrue(String keyword, Pageable pageable);
    boolean existsByNameAndCategoryIdNot(String name, Long categoryId);
}

