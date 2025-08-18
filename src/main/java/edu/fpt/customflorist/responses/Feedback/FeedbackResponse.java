package edu.fpt.customflorist.responses.Feedback;

import edu.fpt.customflorist.models.Feedback;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponse {
    private Long feedbackId;
    private Short rating;
    private String comment;
    private LocalDateTime createdAt;
    private Boolean isActive;
    private Long userId;
    private String userName;  // Added userName field
    private Long bouquetId;
    private Long orderItemId;

    public static FeedbackResponse convertToDTO(Feedback feedback) {
        return new FeedbackResponse(
                feedback.getFeedbackId(),
                feedback.getRating(),
                feedback.getComment(),
                feedback.getCreatedAt(),
                feedback.getIsActive(),
                feedback.getUser().getUserId(),
                feedback.getUser().getName(),  // Added userName
                feedback.getBouquet().getBouquetId(),
                feedback.getOrderItem().getOrderItemId()
        );
    }
}
