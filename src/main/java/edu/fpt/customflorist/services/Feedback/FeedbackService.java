package edu.fpt.customflorist.services.Feedback;

import edu.fpt.customflorist.dtos.Feedback.DeleteFeedbackDTO;
import edu.fpt.customflorist.dtos.Feedback.FeedbackDTO;
import edu.fpt.customflorist.dtos.Feedback.UpdateFeedbackDTO;
import edu.fpt.customflorist.models.Bouquet;
import edu.fpt.customflorist.models.Enums.DeliveryStatus;
import edu.fpt.customflorist.models.Feedback;
import edu.fpt.customflorist.models.OrderItem;
import edu.fpt.customflorist.models.User;
import edu.fpt.customflorist.repositories.BouquetRepository;
import edu.fpt.customflorist.repositories.FeedbackRepository;
import edu.fpt.customflorist.repositories.OrderItemRepository;
import edu.fpt.customflorist.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FeedbackService implements IFeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final BouquetRepository bouquetRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Feedback createFeedback(FeedbackDTO feedbackDTO) throws Exception {
        User user = userRepository.findById(feedbackDTO.getUserId())
                .orElseThrow(() -> new Exception("User not found"));
        Bouquet bouquet = bouquetRepository.findById(feedbackDTO.getBouquetId())
                .orElseThrow(() -> new Exception("Bouquet not found"));
        OrderItem orderItem = orderItemRepository.findById(feedbackDTO.getOrderItemId())
                .orElseThrow(() -> new Exception("OrderItem not found"));

        if (!orderItem.getOrder().getUser().getUserId().equals(user.getUserId())) {
            throw new Exception("You can only leave feedback for items you purchased.");
        }

        if (!orderItem.getOrder().getStatus().name().equals(DeliveryStatus.DELIVERED.name())) {
            throw new Exception("You can only leave feedback after order completion.");
        }

        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setBouquet(bouquet);
        feedback.setOrderItem(orderItem);
        feedback.setRating(feedbackDTO.getRating());
        feedback.setComment(feedbackDTO.getComment());
        feedback.setCreatedAt(LocalDateTime.now());
        feedback.setIsActive(true);

        return feedbackRepository.save(feedback);
    }

    @Override
    public Feedback updateFeedback(Long feedbackId, UpdateFeedbackDTO updateFeedbackDTO) throws Exception {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new Exception("Feedback not found"));

        feedback.setRating(updateFeedbackDTO.getRating());
        feedback.setComment(updateFeedbackDTO.getComment());

        return feedbackRepository.save(feedback);
    }

    @Override
    public void deleteFeedback(Long feedbackId, DeleteFeedbackDTO deleteFeedbackDTO) throws Exception {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new Exception("Feedback not found"));

        feedback.setIsActive(deleteFeedbackDTO.getIsActive());

        feedbackRepository.save(feedback);
    }

    @Override
    public Feedback getFeedbackById(Long feedbackId) throws Exception {
        return feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new Exception("Feedback not found"));
    }

    @Override
    public Page<Feedback> getAllFeedbacks(LocalDateTime startDate, LocalDateTime endDate, Long userId, Pageable pageable) {
        return feedbackRepository.findAllByDateRange(startDate, endDate, userId, pageable);
    }

    @Override
    public Page<Feedback> getFeedbacksByBouquetId(Long bouquetId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return feedbackRepository.findByBouquetIdAndDateRange(bouquetId, startDate, endDate, pageable);
    }

    @Override
    public Page<Feedback> getFeedbacksByUserId(Long userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return feedbackRepository.findByUserIdAndDateRange(userId, startDate, endDate, pageable);
    }

    @Override
    public Page<Feedback> getFeedbackIsActiveByBouquetId(Long bouquetId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return feedbackRepository.findIsActiveByBouquetIdAndDateRange(bouquetId, startDate, endDate, pageable);
    }

    @Override
    public Page<Feedback> getFeedbackIsActiveByUserId(Long userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return feedbackRepository.findIsActiveByUserIdAndDateRange(userId, startDate, endDate, pageable);
    }

}
