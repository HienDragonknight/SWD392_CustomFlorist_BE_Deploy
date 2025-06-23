package edu.fpt.customflorist.services.Feedback;

import edu.fpt.customflorist.dtos.Feedback.DeleteFeedbackDTO;
import edu.fpt.customflorist.dtos.Feedback.FeedbackDTO;
import edu.fpt.customflorist.dtos.Feedback.UpdateFeedbackDTO;
import edu.fpt.customflorist.models.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface IFeedbackService {
    Feedback createFeedback(FeedbackDTO feedbackDTO) throws Exception;
    Feedback updateFeedback(Long feedbackId, UpdateFeedbackDTO updateFeedbackDTO) throws Exception;
    void deleteFeedback(Long feedbackId, DeleteFeedbackDTO deleteFeedbackDTO) throws Exception;
    Page<Feedback> getAllFeedbacks(LocalDateTime startDate, LocalDateTime endDate, Long userId, Pageable pageable);
    Feedback getFeedbackById(Long feedbackId) throws Exception;
    Page<Feedback> getFeedbacksByBouquetId(Long bouquetId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    Page<Feedback> getFeedbacksByUserId(Long userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    Page<Feedback> getFeedbackIsActiveByUserId(Long userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    Page<Feedback> getFeedbackIsActiveByBouquetId(Long bouquetId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
