package edu.fpt.customflorist.repositories;

import edu.fpt.customflorist.models.OrderBouquetFlower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderBouquetFlowerRepository extends JpaRepository<OrderBouquetFlower, Long> {
}
