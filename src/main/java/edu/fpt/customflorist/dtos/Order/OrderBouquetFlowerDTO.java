package edu.fpt.customflorist.dtos.Order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderBouquetFlowerDTO {
    @NotNull(message = "Flower ID cannot be null")
    @Schema(description = "ID of the flower", example = "101")
    private Long flowerId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Schema(description = "Quantity of the flower in the bouquet", example = "5")
    private Integer quantity;
}
