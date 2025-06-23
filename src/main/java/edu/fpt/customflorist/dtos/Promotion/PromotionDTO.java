package edu.fpt.customflorist.dtos.Promotion;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PromotionDTO {
    private Long id;
    private String code;
    private BigDecimal discountPercentage;
    private LocalDate validFrom;
    private LocalDate validTo;
    private boolean isActive;
//    private Long bouquetId;
//    private Integer usageLimit;
//    private Integer currentUsage;
//    private BigDecimal minimumOrderAmount;
}