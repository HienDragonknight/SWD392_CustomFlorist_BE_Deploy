package edu.fpt.customflorist.dtos.Feedback;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteFeedbackDTO {
    @NotNull(message = "Active status is required")
    private Boolean isActive;
}
