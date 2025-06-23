package edu.fpt.customflorist.services.Flower;

import edu.fpt.customflorist.dtos.Flower.FlowerDTO;
import edu.fpt.customflorist.exceptions.DataNotFoundException;
import edu.fpt.customflorist.models.Flower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface IFlowerService {
    Flower createFlower(FlowerDTO flowerDTO);
    Flower updateFlower(Long flowerId, FlowerDTO flowerDTO) throws DataNotFoundException;
    Flower getFlowerById(Long flowerId) throws DataNotFoundException;
    Page<Flower> getAllFlowers(String keyword, String flowerType, String color , BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    Page<Flower> getAllFlowersActive(String keyword, String flowerType, String color, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
}
