package edu.fpt.customflorist.controllers;

import edu.fpt.customflorist.dtos.Feedback.DeleteFeedbackDTO;
import edu.fpt.customflorist.dtos.Feedback.FeedbackDTO;
import edu.fpt.customflorist.dtos.Feedback.UpdateFeedbackDTO;
import edu.fpt.customflorist.models.Feedback;
import edu.fpt.customflorist.responses.Feedback.FeedbackResponse;
import edu.fpt.customflorist.responses.ResponseObject;
import edu.fpt.customflorist.services.Feedback.IFeedbackService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("${api.prefix}/api/v1/feedbacks")
@RequiredArgsConstructor
@CrossOrigin(origins = {"*", "http://localhost:3000", "https://yourflorist.vercel.app"})
public class FeedbackController {
    private final IFeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<ResponseObject> createFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        try {
            Feedback feedback = feedbackService.createFeedback(feedbackDTO);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .message("Feedback created successfully")
                            .status(HttpStatus.OK)
                            .data(FeedbackResponse.convertToDTO(feedback))
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message("Failed to create feedback: " + e.getMessage())
                            .status(HttpStatus.BAD_REQUEST)
                            .data(null)
                            .build()
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateFeedback(@PathVariable Long id, @RequestBody UpdateFeedbackDTO updateFeedbackDTO) {
        try {
            Feedback feedback = feedbackService.updateFeedback(id, updateFeedbackDTO);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .message("Feedback updated successfully")
                            .status(HttpStatus.OK)
                            .data(FeedbackResponse.convertToDTO(feedback))
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message("Failed to update feedback: " + e.getMessage())
                            .status(HttpStatus.BAD_REQUEST)
                            .data(null)
                            .build()
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteFeedback(@PathVariable Long id, @RequestBody DeleteFeedbackDTO deleteFeedbackDTO) {
        try {
            feedbackService.deleteFeedback(id, deleteFeedbackDTO);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .message("Feedback deleted successfully")
                            .status(HttpStatus.OK)
                            .data(null)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message("Failed to delete feedback: " + e.getMessage())
                            .status(HttpStatus.BAD_REQUEST)
                            .data(null)
                            .build()
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getFeedbackById(@PathVariable Long id) {
        try {
            Feedback feedback = feedbackService.getFeedbackById(id);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .message("Feedback retrieved successfully")
                            .status(HttpStatus.OK)
                            .data(FeedbackResponse.convertToDTO(feedback))
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message("Failed to retrieve feedback: " + e.getMessage())
                            .status(HttpStatus.BAD_REQUEST)
                            .data(null)
                            .build()
            );
        }
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getAllFeedbacks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @Parameter(
                    description = "Start date for filtering feedbacks (Format: yyyy-MM-dd'T'HH:mm:ss)",
                    example = "2024-02-20T08:30:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(
                    description = "End date for filtering feedbacks (Format: yyyy-MM-dd'T'HH:mm:ss)",
                    example = "2024-02-21T08:30:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) Long userId,
            @Parameter(description = "Field to sort by, can be 'createdAt' or 'rating'", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort order, 'asc' for ascending, 'desc' for descending", example = "desc")
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("asc")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

            Pageable pageable = PageRequest.of(page, size, sort);
            Page<FeedbackResponse> feedbacks = feedbackService.getAllFeedbacks(startDate, endDate, userId, pageable)
                    .map(FeedbackResponse::convertToDTO);

            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .message("All feedbacks retrieved successfully")
                            .status(HttpStatus.OK)
                            .data(feedbacks)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message("Failed to retrieve feedbacks: " + e.getMessage())
                            .status(HttpStatus.BAD_REQUEST)
                            .data(null)
                            .build()
            );
        }
    }

    @GetMapping("/bouquet/{bouquetId}")
    public ResponseEntity<ResponseObject> getFeedbacksByBouquetId(
            @PathVariable Long bouquetId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "Field to sort by, can be 'createdAt' or 'rating'", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort order, 'asc' for ascending, 'desc' for descending", example = "desc")
            @RequestParam(defaultValue = "desc") String sortDir

    ) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("asc")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

            Pageable pageable = PageRequest.of(page, size, sort);
            Page<FeedbackResponse> feedbacks = feedbackService.getFeedbacksByBouquetId(bouquetId, startDate, endDate, pageable)
                    .map(FeedbackResponse::convertToDTO);

            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .message("Feedbacks for bouquet retrieved successfully")
                            .status(HttpStatus.OK)
                            .data(feedbacks)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message("Failed to retrieve feedbacks: " + e.getMessage())
                            .status(HttpStatus.BAD_REQUEST)
                            .data(null)
                            .build()
            );
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseObject> getFeedbacksByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "Field to sort by, can be 'createdAt' or 'rating'", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort order, 'asc' for ascending, 'desc' for descending", example = "desc")
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("asc")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

            Pageable pageable = PageRequest.of(page, size, sort);
            Page<FeedbackResponse> feedbacks = feedbackService.getFeedbacksByUserId(userId, startDate, endDate, pageable)
                    .map(FeedbackResponse::convertToDTO);

            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .message("Feedbacks for user retrieved successfully")
                            .status(HttpStatus.OK)
                            .data(feedbacks)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message("Failed to retrieve feedbacks: " + e.getMessage())
                            .status(HttpStatus.BAD_REQUEST)
                            .data(null)
                            .build()
            );
        }
    }

    @GetMapping("/bouquet/{bouquetId}/active")
    public ResponseEntity<ResponseObject> getFeedbackIsActiveByBouquetId(
            @PathVariable Long bouquetId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "Field to sort by, can be 'createdAt' or 'rating'", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort order, 'asc' for ascending, 'desc' for descending", example = "desc")
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("asc")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

            Pageable pageable = PageRequest.of(page, size, sort);
            Page<FeedbackResponse> isActiveList = feedbackService.getFeedbackIsActiveByBouquetId(bouquetId, startDate, endDate, pageable).map(FeedbackResponse::convertToDTO);

            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .message("Feedback isActive status for bouquet retrieved successfully")
                            .status(HttpStatus.OK)
                            .data(isActiveList)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message("Failed to retrieve isActive status: " + e.getMessage())
                            .status(HttpStatus.BAD_REQUEST)
                            .data(null)
                            .build()
            );
        }
    }

    @GetMapping("/user/{userId}/active")
    public ResponseEntity<ResponseObject> getFeedbackIsActiveByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "Field to sort by, can be 'createdAt' or 'rating'", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort order, 'asc' for ascending, 'desc' for descending", example = "desc")
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("asc")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

            Pageable pageable = PageRequest.of(page, size, sort);
            Page<FeedbackResponse> isActiveList = feedbackService.getFeedbackIsActiveByUserId(userId, startDate, endDate, pageable).map(FeedbackResponse::convertToDTO);

            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .message("Feedback isActive status for user retrieved successfully")
                            .status(HttpStatus.OK)
                            .data(isActiveList)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message("Failed to retrieve isActive status: " + e.getMessage())
                            .status(HttpStatus.BAD_REQUEST)
                            .data(null)
                            .build()
            );
        }
    }

}
