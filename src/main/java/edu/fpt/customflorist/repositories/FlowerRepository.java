package edu.fpt.customflorist.repositories;

import edu.fpt.customflorist.models.Flower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FlowerRepository extends JpaRepository<Flower, Long> {
    @Query("SELECT COUNT(f) FROM Flower f WHERE f.isActive = true")
    long countActiveFlowers();

    @Query("SELECT COUNT(f) FROM Flower f WHERE f.isActive = false")
    long countInactiveFlowers();

    @Query("SELECT f FROM Flower f WHERE " +
            "(:keyword IS NULL OR f.name LIKE %:keyword%) AND " +
            "(:flowerType IS NULL OR LOWER(f.flowerType) = LOWER(:flowerType)) AND " +
            "(:color IS NULL OR LOWER(f.color) = LOWER(:color)) AND " +
            "(:minPrice IS NULL OR f.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR f.price <= :maxPrice)")
    Page<Flower> findAllFlowers(@Param("keyword") String keyword,
                                @Param("flowerType") String flowerType,
                                @Param("color") String color,
                                @Param("minPrice") BigDecimal minPrice,
                                @Param("maxPrice") BigDecimal maxPrice,
                                Pageable pageable);

    @Query("SELECT f FROM Flower f WHERE f.isActive = true AND " +
            "(:keyword IS NULL OR f.name LIKE %:keyword%) AND " +
            "(:flowerType IS NULL OR LOWER(f.flowerType) = LOWER(:flowerType)) AND " +
            "(:color IS NULL OR LOWER(f.color) = LOWER(:color)) AND " +
            "(:minPrice IS NULL OR f.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR f.price <= :maxPrice)")
    Page<Flower> findAllFlowersActive(@Param("keyword") String keyword,
                                      @Param("flowerType") String flowerType,
                                      @Param("color") String color,
                                      @Param("minPrice") BigDecimal minPrice,
                                      @Param("maxPrice") BigDecimal maxPrice,
                                      Pageable pageable);
}
