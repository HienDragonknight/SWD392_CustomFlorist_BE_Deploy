package edu.fpt.customflorist.dtos.Feedback;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackDTO {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Bouquet ID is required")
    private Long bouquetId;

    @NotNull(message = "Order Item ID is required")
    private Long orderItemId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Short rating;

    @NotBlank(message = "Comment is required")
    private String comment;

    @NotNull(message = "Active status is required")
    private Boolean isActive;
}
