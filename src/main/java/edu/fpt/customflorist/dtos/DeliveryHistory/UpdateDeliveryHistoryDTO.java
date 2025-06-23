package edu.fpt.customflorist.dtos.DeliveryHistory;

import edu.fpt.customflorist.models.Enums.DeliveryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDeliveryHistoryDTO {

    @NotNull(message = "Delivery date cannot be null")
    @Schema(description = "Delivery date (ISO-8601 format)", example = "2024-03-10T14:30:00")
    private LocalDateTime deliveryDate;

    @NotNull(message = "Delivery status cannot be null")
    @Schema(
            description = "Delivery status",
            example = "SHIPPED, DELIVERED, CANCELLED, SKIP",
            allowableValues = { "SHIPPED", "DELIVERED", "CANCELLED", "SKIP" }
    )
    private DeliveryStatus status;
    private String note;
}
