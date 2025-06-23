package edu.fpt.customflorist.repositories;

import edu.fpt.customflorist.models.BouquetComposition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BouquetCompositionRepository extends JpaRepository<BouquetComposition, Long> {
//    void deleteAllByBouquet(List<BouquetComposition> bouquetCompositions);
}
