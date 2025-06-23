package edu.fpt.customflorist.dtos.Order;

import edu.fpt.customflorist.models.Enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderUpdateDTO {
    @NotNull(message = "Status cannot be null")
    @Schema(
            example = "Available values: PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED"
    )
    Status status;
    String reason;
}
