package edu.fpt.customflorist.controllers;

import edu.fpt.customflorist.dtos.Order.OrderDTO;
import edu.fpt.customflorist.dtos.Order.OrderUpdateDTO;
import edu.fpt.customflorist.exceptions.DataNotFoundException;
import edu.fpt.customflorist.models.Order;
import edu.fpt.customflorist.responses.Order.OrderResponse;
import edu.fpt.customflorist.responses.ResponseObject;
import edu.fpt.customflorist.services.Order.IOrderService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/api/v1/orders")
@CrossOrigin(origins = {"*", "http://localhost:3000", "https://yourflorist.vercel.app"})
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO) {
        try {
            Order createdOrder = orderService.createOrder(orderDTO);
            OrderResponse orderResponse = orderService.convertToOrderResponse(createdOrder);
            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .message("Order created successfully")
                            .data(orderResponse)
                            .status(HttpStatus.CREATED)
                            .build()
            );
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseObject.builder()
                            .message(e.getMessage())
                            .status(HttpStatus.NOT_FOUND)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ResponseObject.builder()
                            .message("Error creating order: " + e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build()
            );
        }
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestBody OrderUpdateDTO status) {
        try {
            orderService.updateOrder(orderId, status);
            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .message("Order status updated successfully")
                            .status(HttpStatus.OK)
                            .build()
            );
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseObject.builder()
                            .message(e.getMessage())
                            .status(HttpStatus.NOT_FOUND)
                            .build()
            );
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseObject.builder()
                            .message(e.getMessage())
                            .status(HttpStatus.BAD_REQUEST)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ResponseObject.builder()
                            .message("Error updating order status: " + e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build()
            );
        }
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .message("Order deleted successfully")
                            .status(HttpStatus.OK)
                            .build()
            );
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseObject.builder()
                            .message(e.getMessage())
                            .status(HttpStatus.NOT_FOUND)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ResponseObject.builder()
                            .message("Error deleting order: " + e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build()
            );
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId) {
        try {
            Order order = orderService.getOrderById(orderId);
            OrderResponse orderResponse = orderService.convertToOrderResponse(order);
            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .message("Order retrieved successfully")
                            .data(orderResponse)
                            .status(HttpStatus.OK)
                            .build()
            );
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseObject.builder()
                            .message(e.getMessage())
                            .status(HttpStatus.NOT_FOUND)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ResponseObject.builder()
                            .message("Error retrieving order: " + e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build()
            );
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllOrdersActive(
            @PathVariable Long userId,
            @Parameter(description = "Order filtering start date (ISO-8601: yyyy-MM-dd'T'HH:mm:ss)", example = "2024-03-01T00:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime minOrderDate,
            @Parameter(description = "Order filtering end date (ISO-8601: yyyy-MM-dd'T'HH:mm:ss)", example = "2024-03-07T23:59:59")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime maxOrderDate,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Order status filter: PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED")
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @Parameter(description = "Sort direction (ASC or DESC), default is ASC", example = "ASC")
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "orderId"));
            Page<Order> orders = orderService.getAllOrdersActive(userId, minOrderDate, maxOrderDate, minPrice, maxPrice, status, pageable);
            Page<OrderResponse> orderResponses = orders.map(orderService::convertToOrderResponse);

            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .message("Orders retrieved successfully")
                            .data(orderResponses)
                            .status(HttpStatus.OK)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ResponseObject.builder()
                            .message("Error retrieving orders: " + e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build()
            );
        }
    }

    @GetMapping("/active/courier")
    public ResponseEntity<?> getAllOrdersActiveFoDelivery(
            @Parameter(description = "Order filtering start date (ISO-8601: yyyy-MM-dd'T'HH:mm:ss)", example = "2024-03-01T00:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime minOrderDate,
            @Parameter(description = "Order filtering end date (ISO-8601: yyyy-MM-dd'T'HH:mm:ss)", example = "2024-03-07T23:59:59")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime maxOrderDate,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Order status filter: PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED")
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String customerName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @Parameter(description = "Sort direction (ASC or DESC), default is ASC", example = "ASC")
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "orderId"));
            Page<Order> orders = orderService.getAllOrdersActiveFoDelivery(minOrderDate, maxOrderDate, minPrice, maxPrice, status, customerName, pageable);
            Page<OrderResponse> orderResponses = orders.map(orderService::convertToOrderResponse);

            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .message("Orders retrieved successfully")
                            .data(orderResponses)
                            .status(HttpStatus.OK)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ResponseObject.builder()
                            .message("Error retrieving orders: " + e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build()
            );
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders(
            @Parameter(description = "Order filtering start date (ISO-8601: yyyy-MM-dd'T'HH:mm:ss)", example = "2024-03-01T00:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime minOrderDate,
            @Parameter(description = "Order filtering end date (ISO-8601: yyyy-MM-dd'T'HH:mm:ss)", example = "2024-03-07T23:59:59")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime maxOrderDate,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Order status filter: PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED")
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String phone,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @Parameter(description = "Sort direction (ASC or DESC), default is ASC", example = "ASC")
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "orderId"));
            Page<Order> orders = orderService.getAllOrders(minOrderDate, maxOrderDate, minPrice, maxPrice, status, userId, userName, phone, pageable);
            Page<OrderResponse> orderResponses = orders.map(orderService::convertToOrderResponse);

            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .message("Orders retrieved successfully")
                            .data(orderResponses)
                            .status(HttpStatus.OK)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ResponseObject.builder()
                            .message("Error retrieving orders: " + e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build()
            );
        }
    }

}
