package edu.fpt.customflorist.controllers;

import edu.fpt.customflorist.dtos.DeliveryHistory.DeleteDeliveryHistoryDTO;
import edu.fpt.customflorist.dtos.DeliveryHistory.DeliveryHistoryDTO;
import edu.fpt.customflorist.dtos.DeliveryHistory.UpdateDeliveryHistoryDTO;
import edu.fpt.customflorist.models.DeliveryHistory;
import edu.fpt.customflorist.models.DeliveryStatusHistory;
import edu.fpt.customflorist.responses.DeliveryHistory.DeliveryHistoryResponse;
import edu.fpt.customflorist.responses.DeliveryHistory.DeliveryStatusHistoryResponse;
import edu.fpt.customflorist.responses.ResponseObject;
import edu.fpt.customflorist.services.DeliveryHistory.IDeliveryHistoryService;
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
@RequestMapping("${api.prefix}/api/v1/delivery-histories")
@RequiredArgsConstructor
@CrossOrigin(origins = {"*", "http://localhost:3000", "https://yourflorist.vercel.app"})
public class DeliveryHistoryController {
    private final IDeliveryHistoryService deliveryHistoryService;

    @PostMapping
    public ResponseEntity<ResponseObject> createDeliveryHistory(@RequestBody DeliveryHistoryDTO dto) {
        try {
            DeliveryHistory deliveryHistory = deliveryHistoryService.createDeliveryHistory(dto);

            return ResponseEntity.ok(new ResponseObject(
                    "Delivery history created successfully",
                    HttpStatus.OK,
                    DeliveryHistoryResponse.fromEntity(deliveryHistory))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("Failed to create delivery history", HttpStatus.BAD_REQUEST, null));
        }
    }

    @PutMapping("/{deliveryId}")
    public ResponseEntity<ResponseObject> updateDeliveryHistory(
            @PathVariable Long deliveryId, @RequestBody UpdateDeliveryHistoryDTO dto) {
        try {
            DeliveryStatusHistory statusHistory = deliveryHistoryService.updateDeliveryHistory(deliveryId, dto);

            return ResponseEntity.ok(new ResponseObject(
                    "Delivery history updated successfully",
                    HttpStatus.OK,
                    DeliveryStatusHistoryResponse.fromEntity(statusHistory))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject(e.getMessage(), HttpStatus.BAD_REQUEST, null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getDeliveryHistoryById(@PathVariable Long id) {
        try {
            DeliveryHistory deliveryHistory = deliveryHistoryService.getDeliveryHistoryById(id);

            return ResponseEntity.ok(new ResponseObject(
                    "Delivery history retrieved successfully",
                    HttpStatus.OK,
                    DeliveryHistoryResponse.fromEntity(deliveryHistory))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("Delivery history not found", HttpStatus.NOT_FOUND, null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteDeliveryHistory(@PathVariable Long id, @RequestBody DeleteDeliveryHistoryDTO dto) {
        try {
            deliveryHistoryService.deleteDeliveryHistory(id, dto);

            return ResponseEntity.ok(new ResponseObject("Delivery history deleted successfully", HttpStatus.OK, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("Delivery history not found", HttpStatus.NOT_FOUND, null));
        }
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getAllDeliveryHistories(
            @Parameter(
                    description = "Start date for filtering delivery histories (Format: yyyy-MM-dd'T'HH:mm:ss)",
                    example = "2024-02-20T08:30:00")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,

            @Parameter(
                    description = "End date for filtering delivery histories (Format: yyyy-MM-dd'T'HH:mm:ss)",
                    example = "2024-02-21T18:00:00")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long courierId,
            @Parameter(description = "DeliveryHistory status filter: PENDING, SHIPPED, DELIVERED, CANCELLED, SKIP")
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @Parameter(description = "Sort direction (ASC or DESC), default is ASC", example = "ASC")
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "deliveryId"));

            Page<DeliveryHistory> pages = deliveryHistoryService.getAllDeliveryHistories(startDate, endDate, userId, courierId, status, pageable);

            return ResponseEntity.ok(new ResponseObject(
                    "Delivery history list retrieved successfully",
                    HttpStatus.OK,
                    pages.map(DeliveryHistoryResponse::fromEntity))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject("An error occurred while retrieving delivery histories",
                            HttpStatus.INTERNAL_SERVER_ERROR, null));
        }
    }

    @GetMapping("/active/user/{userId}")
    public ResponseEntity<ResponseObject> getAllActiveByUserId(
            @PathVariable Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "DeliveryHistory status filter: PENDING, SHIPPED, DELIVERED, CANCELLED, SKIP")
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @Parameter(description = "Sort direction (ASC or DESC), default is ASC", example = "ASC")
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "deliveryId"));

            Page<DeliveryHistory> pages = deliveryHistoryService.getAllActiveByUserId(userId, startDate, endDate, status, pageable);

            return ResponseEntity.ok(new ResponseObject(
                    "User's active delivery history retrieved successfully",
                    HttpStatus.OK,
                    pages.map(DeliveryHistoryResponse::fromEntity))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject("An error occurred while retrieving user's active delivery history",
                            HttpStatus.INTERNAL_SERVER_ERROR, null));
        }
    }

    @GetMapping("/active/courier/{courierId}")
    public ResponseEntity<ResponseObject> getAllActiveByCourierId(
            @PathVariable Long courierId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "DeliveryHistory status filter: PENDING, SHIPPED, DELIVERED, CANCELLED, SKIP")
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @Parameter(description = "Sort direction (ASC or DESC), default is ASC", example = "ASC")
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "deliveryId"));

            Page<DeliveryHistory> pages = deliveryHistoryService.getAllActiveByCourierId(courierId, startDate, endDate, status, pageable);

            return ResponseEntity.ok(new ResponseObject(
                    "Courier's active delivery history retrieved successfully",
                    HttpStatus.OK,
                    pages.map(DeliveryHistoryResponse::fromEntity))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject("An error occurred while retrieving courier's active delivery history",
                            HttpStatus.INTERNAL_SERVER_ERROR, null));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseObject> getAllByUserId(
            @PathVariable Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "DeliveryHistory status filter: PENDING, SHIPPED, DELIVERED, CANCELLED, SKIP")
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @Parameter(description = "Sort direction (ASC or DESC), default is ASC", example = "ASC")
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "deliveryId"));

            Page<DeliveryHistory> pages = deliveryHistoryService.getAllByUserId(userId, startDate, endDate, status, pageable);

            return ResponseEntity.ok(new ResponseObject(
                    "User's delivery history retrieved successfully",
                    HttpStatus.OK,
                    pages.map(DeliveryHistoryResponse::fromEntity))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject("An error occurred while retrieving user's delivery history",
                            HttpStatus.INTERNAL_SERVER_ERROR, null));
        }
    }

    @GetMapping("/courier/{courierId}")
    public ResponseEntity<ResponseObject> getAllByCourierId(
            @PathVariable Long courierId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "DeliveryHistory status filter: PENDING, SHIPPED, DELIVERED, CANCELLED, SKIP")
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @Parameter(description = "Sort direction (ASC or DESC), default is ASC", example = "ASC")
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "deliveryId"));

            Page<DeliveryHistory> pages = deliveryHistoryService.getAllByCourierId(courierId, startDate, endDate, status, pageable);

            return ResponseEntity.ok(new ResponseObject(
                    "Courier's delivery history retrieved successfully",
                    HttpStatus.OK,
                    pages.map(DeliveryHistoryResponse::fromEntity))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject("An error occurred while retrieving courier's delivery history",
                            HttpStatus.INTERNAL_SERVER_ERROR, null));
        }
    }

}
