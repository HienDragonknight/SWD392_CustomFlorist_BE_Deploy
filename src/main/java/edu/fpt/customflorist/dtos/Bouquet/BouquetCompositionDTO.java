package edu.fpt.customflorist.dtos.Bouquet;

import lombok.Data;

@Data
public class BouquetCompositionDTO {
    private Long id;
    private Long flowerId;
    private Integer minQuantity;
    private Integer maxQuantity;
    private Integer quantity;
    private Boolean isActive;
}
