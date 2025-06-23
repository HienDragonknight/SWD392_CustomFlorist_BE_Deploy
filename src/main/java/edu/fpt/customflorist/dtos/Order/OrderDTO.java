package edu.fpt.customflorist.dtos.Order;

import edu.fpt.customflorist.models.Enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    @NotNull(message = "User ID cannot be null")
    @Schema(description = "ID of the user placing the order", example = "123")
    private Long userId;

    @Schema(description = "ID of the applied promotion (optional)", example = "10")
    private Long promotionId;

    @NotNull(message = "Total price cannot be null")
    @Min(value = 0, message = "Total price must be at least 0")
    @Schema(description = "Total price of the order", example = "150.75")
    private BigDecimal totalPrice;

    @NotBlank(message = "Shipping address cannot be blank")
    @Size(max = 255, message = "Shipping address must not exceed 255 characters")
    @Schema(description = "Shipping address for the order", example = "123 Main Street, City, Country")
    private String shippingAddress;

    @NotNull(message = "Order items cannot be null")
    @Size(min = 1, message = "Order must have at least one item")
    @Valid
    @Schema(description = "List of order items")
    private List<OrderItemDTO> orderItems;
}