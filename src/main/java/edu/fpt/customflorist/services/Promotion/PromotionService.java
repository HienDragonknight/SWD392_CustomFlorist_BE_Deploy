package edu.fpt.customflorist.services.Promotion;

import edu.fpt.customflorist.dtos.Promotion.PromotionDTO;
import edu.fpt.customflorist.dtos.Promotion.PromotionDTO;
import edu.fpt.customflorist.dtos.Promotion.PromotionRequestDTO;
import edu.fpt.customflorist.exceptions.DataNotFoundException;
import edu.fpt.customflorist.models.Bouquet;
import edu.fpt.customflorist.models.Promotion;
import edu.fpt.customflorist.repositories.BouquetRepository;
import edu.fpt.customflorist.repositories.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionService implements IPromotionService {
    private final PromotionRepository promotionRepository;
    private final BouquetRepository bouquetRepository;
    @Override
    public List<PromotionDTO> getAllPromotions() {
        return promotionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<PromotionDTO> getActivePromotions() {
        LocalDate now = LocalDate.now();
        return promotionRepository.findPromotions(now).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public PromotionDTO getPromotionById(Long id) throws DataNotFoundException {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Promotion not found with id: " + id));
        return convertToDTO(promotion);
    }
    @Override
    public PromotionDTO findPromotionByCode(String code) throws DataNotFoundException{
        Promotion promotion = promotionRepository.findByPromotionCode(code)
                .orElseThrow(() -> new DataNotFoundException("Promotion not found with code: " + code));
        return convertToDTO(promotion);
    }
    @Override
    @Transactional
    public PromotionDTO createPromotion(PromotionRequestDTO promotionDTO) {
        Promotion promotion = new Promotion();
        promotion.setPromotionCode(promotionDTO.getCode());
        promotion.setDiscountPercentage(promotionDTO.getDiscountPercentage());
        promotion.setValidFrom(promotionDTO.getValidFrom());
        promotion.setValidTo(promotionDTO.getValidTo());
        promotion.setIsActive(promotionDTO.isActive());

//        // Thiết lập mối quan hệ với Bouquet nếu có
//        if (promotionDTO.getBouquetId() != null) {
//            Bouquet bouquet = bouquetRepository.findById(promotionDTO.getBouquetId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Bouquet not found with id: " + promotionDTO.getBouquetId()));
//            promotion.setBouquet(bouquet);
//        }

        Promotion savedPromotion = promotionRepository.save(promotion);
        return convertToDTO(savedPromotion);
    }
    @Override
    @Transactional
    public PromotionDTO updatePromotion(Long id, PromotionRequestDTO promotionDTO) throws DataNotFoundException {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Promotion not found with id: " + id));

        promotion.setPromotionCode(promotionDTO.getCode());
        promotion.setDiscountPercentage(promotionDTO.getDiscountPercentage());
        promotion.setValidFrom(promotionDTO.getValidFrom());
        promotion.setValidTo(promotionDTO.getValidTo());
        promotion.setIsActive(promotionDTO.isActive());

//        // Cập nhật mối quan hệ với Bouquet
//        if (promotionDTO.getBouquetId() != null) {
//            Bouquet bouquet = bouquetRepository.findById(promotionDTO.getBouquetId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Bouquet not found with id: " + promotionDTO.getBouquetId()));
//            promotion.setBouquet(bouquet);
//        } else {
//            promotion.setBouquet(null);
//        }

        Promotion updatedPromotion = promotionRepository.save(promotion);
        return convertToDTO(updatedPromotion);
    }
    @Override
    @Transactional
    public void deletePromotion(Long id) throws DataNotFoundException {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Promotion not found with id: " + id));

        promotionRepository.delete(promotion);
    }

//    @Transactional
//    public boolean applyPromotion(String code) {
//        Promotion promotion = promotionRepository.findByPromotionCode(code)
//                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found with code: " + code));
//
//        LocalDateTime now = LocalDateTime.now();
//
//        // Kiểm tra xem khuyến mãi có còn hiệu lực không
//        if (!promotion.getIsActive() ||
//                now.isBefore(promotion.getValidFrom())) ||
//                now.isAfter(promotion.getValidTo())) {
//            return false;
//        }
//
//        // Kiểm tra số lần sử dụng
//        if (promotion.getUsageLimit() != null &&
//                promotion.getCurrentUsage() >= promotion.getUsageLimit()) {
//            return false;
//        }
//
//        // Tăng số lần sử dụng
//        promotion.setCurrentUsage(promotion.getCurrentUsage() + 1);
//        promotionRepository.save(promotion);
//
//        return true;
//    }

    private PromotionDTO convertToDTO(Promotion promotion) {
        PromotionDTO dto = new PromotionDTO();
        dto.setId(promotion.getPromotionId());
        dto.setCode(promotion.getPromotionCode());
        dto.setDiscountPercentage(promotion.getDiscountPercentage());
        dto.setValidFrom(promotion.getValidFrom());
        dto.setValidTo(promotion.getValidTo());
        dto.setActive(promotion.getIsActive());

        return dto;
    }
}