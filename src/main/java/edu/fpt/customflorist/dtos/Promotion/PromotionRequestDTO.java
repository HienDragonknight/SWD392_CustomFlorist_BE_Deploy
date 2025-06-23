package edu.fpt.customflorist.dtos.Promotion;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PromotionRequestDTO {
    private String code;
    private BigDecimal discountPercentage;
    private LocalDate validFrom;
    private LocalDate validTo;
    private boolean isActive;
}
