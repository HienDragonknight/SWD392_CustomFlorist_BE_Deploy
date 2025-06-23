package edu.fpt.customflorist.services.Bouquet;


import edu.fpt.customflorist.dtos.Bouquet.BouquetCompositionDTO;
import edu.fpt.customflorist.dtos.Bouquet.BouquetCompositionRequestDTO;
import edu.fpt.customflorist.dtos.Bouquet.BouquetDTO;
import edu.fpt.customflorist.dtos.Bouquet.BouquetRequestDTO;
import edu.fpt.customflorist.dtos.Category.CategoryDTO;
import edu.fpt.customflorist.exceptions.DataNotFoundException;
import edu.fpt.customflorist.models.*;
import edu.fpt.customflorist.repositories.BouquetCompositionRepository;
import edu.fpt.customflorist.repositories.BouquetRepository;
import edu.fpt.customflorist.repositories.CategoryRepository;
import edu.fpt.customflorist.repositories.FlowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.fpt.customflorist.models.BouquetComposition;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BouquetService implements IBouquetService {
    private final BouquetRepository bouquetRepository;
    private final BouquetCompositionRepository compositionRepository;
    private final FlowerRepository flowerRepository;
    private final CategoryRepository categoryRepository;


    @Override
    public List<BouquetDTO> getAllBouquets() {
        return bouquetRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<BouquetDTO> getAllActiveBouquets() {
        return bouquetRepository.findAllByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public BouquetDTO getBouquetById(Long id) throws DataNotFoundException {
        Bouquet bouquet = bouquetRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Bouquet not found with id: " + id));
        return convertToDTO(bouquet);
    }

    @Override
    @Transactional
    public BouquetDTO createBouquet(BouquetRequestDTO bouquetDTO) throws DataNotFoundException {
        Bouquet bouquet = new Bouquet();
        bouquet.setName(bouquetDTO.getName());
        bouquet.setDescription(bouquetDTO.getDescription());
        bouquet.setBasePrice(bouquetDTO.getPrice());
        bouquet.setImage(bouquetDTO.getImageUrl());
        bouquet.setIsActive(bouquetDTO.getIsActive());
        Category category = categoryRepository.findById(bouquetDTO.getCategoryId()).orElseThrow(
                () -> new DataNotFoundException("Category not found with id: " + bouquetDTO.getCategoryId())
        );
        bouquet.setCategory(category);

        Bouquet savedBouquet = bouquetRepository.save(bouquet);
        // Xử lý thành phần của bó hoa
        if (bouquetDTO.getCompositions() != null) {
            for (BouquetCompositionRequestDTO compositionDTO : bouquetDTO.getCompositions()) {
                Flower flower = flowerRepository.findById(compositionDTO.getFlowerId())
                        .orElseThrow(() -> new DataNotFoundException("Flower not found with id: " + compositionDTO.getFlowerId()));

                BouquetComposition composition = new BouquetComposition();
                composition.setBouquet(bouquet);
                composition.setFlower(flower);
                composition.setMinQuantity(compositionDTO.getMinQuantity());
                composition.setMaxQuantity(compositionDTO.getQuantity());
                composition.setQuantity(compositionDTO.getQuantity());
                composition.setIsActive(compositionDTO.getIsActive());
                compositionRepository.save(composition);
                savedBouquet.setCompositions(List.of(composition));
            }
        }
        return convertToDTO(savedBouquet);
    }

    @Override
    @Transactional
    public BouquetDTO updateBouquet(Long id, BouquetRequestDTO bouquetDTO) throws DataNotFoundException {
        Bouquet bouquet = bouquetRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Bouquet not found with id: " + id));

        bouquet.setName(bouquetDTO.getName());
        bouquet.setDescription(bouquetDTO.getDescription());
        bouquet.setBasePrice(bouquetDTO.getPrice());
        bouquet.setImage(bouquetDTO.getImageUrl());
        bouquet.setIsActive(bouquetDTO.getIsActive());
        Category category = categoryRepository.findById(bouquetDTO.getCategoryId()).orElseThrow(
                () -> new DataNotFoundException("Category not found with id: " + bouquetDTO.getCategoryId())
        );
        bouquet.setCategory(category);
        // Xóa các thành phần cũ
        compositionRepository.deleteAll(bouquet.getCompositions());
        bouquet.getCompositions().clear();

        // Thêm các thành phần mới
        if (bouquetDTO.getCompositions() != null) {
            for (BouquetCompositionRequestDTO compositionDTO : bouquetDTO.getCompositions()) {
                Flower flower = flowerRepository.findById(compositionDTO.getFlowerId())
                        .orElseThrow(() -> new DataNotFoundException("Flower not found with id: " + compositionDTO.getFlowerId()));

                BouquetComposition composition = new BouquetComposition();
                composition.setBouquet(bouquet);
                composition.setFlower(flower);
                composition.setMaxQuantity(compositionDTO.getMaxQuantity());
                composition.setMinQuantity(compositionDTO.getMinQuantity());
                composition.setQuantity(compositionDTO.getQuantity());
                composition.setIsActive(compositionDTO.getIsActive());
                bouquet.getCompositions().add(composition);
            }
        }

        Bouquet updatedBouquet = bouquetRepository.save(bouquet);
        return convertToDTO(updatedBouquet);
    }

    @Override
    @Transactional
    public void deleteBouquet(Long id) throws DataNotFoundException {
        Bouquet bouquet = bouquetRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Bouquet not found with id: " + id));

        // Xóa các thành phần trước khi xóa bó hoa
        compositionRepository.deleteAll(bouquet.getCompositions());
        bouquetRepository.delete(bouquet);
    }

    private BouquetDTO convertToDTO(Bouquet bouquet) {
        BouquetDTO dto = new BouquetDTO();
        dto.setId(bouquet.getBouquetId());
        dto.setName(bouquet.getName());
        dto.setDescription(bouquet.getDescription());
        dto.setPrice(bouquet.getBasePrice());
        dto.setImageUrl(bouquet.getImage());
        dto.setIsActive(bouquet.getIsActive());

        // Chuyển đổi danh sách thành phần

        List<BouquetCompositionDTO> compositionDTOs = bouquet.getCompositions().stream()
                .map(composition -> {
                    BouquetCompositionDTO compDTO = new BouquetCompositionDTO();
                    compDTO.setId(composition.getBouquetCompositionId());
                    compDTO.setFlowerId(composition.getFlower().getFlowerId());
                    compDTO.setQuantity(composition.getQuantity());
                    compDTO.setMaxQuantity(composition.getMaxQuantity());
                    compDTO.setMinQuantity(composition.getMinQuantity());
                    compDTO.setIsActive(composition.getIsActive());
                    return compDTO;
                })
                .collect(Collectors.toList());
        dto.setCompositions(compositionDTOs);
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(bouquet.getCategory().getCategoryId());
        categoryDTO.setName(bouquet.getCategory().getName());
        categoryDTO.setDescription(bouquet.getCategory().getDescription());
        categoryDTO.setIsActive(bouquet.getCategory().getIsActive());
        dto.setCategory(categoryDTO);

        return dto;
    }
}