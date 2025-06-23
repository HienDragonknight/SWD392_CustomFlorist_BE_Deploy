package edu.fpt.customflorist.repositories;

import edu.fpt.customflorist.models.Promotion;
import edu.fpt.customflorist.models.PromotionManager;
import edu.fpt.customflorist.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromotionManagerRepository extends JpaRepository<PromotionManager, Long> {
    Optional<PromotionManager> findByUserAndPromotion(User user, Promotion promotion);
}
