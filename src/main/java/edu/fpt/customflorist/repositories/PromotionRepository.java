package edu.fpt.customflorist.repositories;

import edu.fpt.customflorist.models.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    @Query(value = "SELECT * FROM promotion WHERE valid_from <= :now AND valid_to >= :now", nativeQuery = true)
    List<Promotion> findPromotions(@Param("now") LocalDate now);
    Optional<Promotion> findByPromotionCode(String code);

}
