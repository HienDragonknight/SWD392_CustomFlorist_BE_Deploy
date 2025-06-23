package edu.fpt.customflorist.repositories;

import edu.fpt.customflorist.models.Bouquet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BouquetRepository extends JpaRepository<Bouquet, Long>  {
    List<Bouquet> findAllByIsActiveTrue();
    @Query("SELECT COUNT(b) FROM Bouquet b WHERE b.isActive = true")
    long countActiveBouquets();

    @Query("SELECT COUNT(b) FROM Bouquet b WHERE b.isActive = false")
    long countInactiveBouquets();

}
