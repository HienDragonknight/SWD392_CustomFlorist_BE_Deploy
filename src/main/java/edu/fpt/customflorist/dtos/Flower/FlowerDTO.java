package edu.fpt.customflorist.dtos.Flower;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowerDTO {
    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Flower type is required")
    private String flowerType;

    @NotNull(message = "Color is required")
    private String color;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotNull(message = "Image is required")
    private String image;

    @NotNull(message = "Active status is required")
    private Boolean isActive;
}
