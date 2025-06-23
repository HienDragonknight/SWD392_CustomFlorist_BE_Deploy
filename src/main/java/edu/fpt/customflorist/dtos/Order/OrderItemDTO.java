package edu.fpt.customflorist.dtos.Order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    @NotNull(message = "Bouquet ID cannot be null")
    @Schema(description = "ID of the bouquet", example = "5")
    private Long bouquetId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Schema(description = "Quantity of the bouquet in the order", example = "2")
    private Integer quantity;

    @NotNull(message = "Subtotal cannot be null")
    @Min(value = 0, message = "Subtotal must be at least 0")
    @Schema(description = "Subtotal price for this order item", example = "99.99")
    private BigDecimal subTotal;

    @NotNull(message = "Order bouquet flowers cannot be null")
    @Size(min = 1, message = "Order item must have at least one flower")
    @Valid
    @Schema(description = "List of flowers included in the bouquet")
    private List<OrderBouquetFlowerDTO> orderBouquetFlowers;
}