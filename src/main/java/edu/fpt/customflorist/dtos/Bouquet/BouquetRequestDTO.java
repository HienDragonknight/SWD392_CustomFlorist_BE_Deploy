package edu.fpt.customflorist.dtos.Bouquet;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BouquetRequestDTO {
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Boolean isActive;
    private List<BouquetCompositionRequestDTO> compositions;
    private Long categoryId;
}
