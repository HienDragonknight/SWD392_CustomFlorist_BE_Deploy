package edu.fpt.customflorist.dtos.Bouquet;

import edu.fpt.customflorist.dtos.Category.CategoryDTO;
import lombok.Data;


@Data
public class BouquetCompositionRequestDTO {
    private Long flowerId;
    private Integer minQuantity;
    private Integer maxQuantity;
    private Integer quantity;
    private Boolean isActive;
}
