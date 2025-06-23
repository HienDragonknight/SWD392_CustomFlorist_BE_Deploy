package edu.fpt.customflorist.dtos.Bouquet;

import edu.fpt.customflorist.dtos.Category.CategoryDTO;
import edu.fpt.customflorist.models.Category;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class BouquetDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Boolean isActive;
    private List<BouquetCompositionDTO> compositions;
    private CategoryDTO category;
}