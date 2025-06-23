package edu.fpt.customflorist.responses.Order;

import edu.fpt.customflorist.models.DeliveryHistory;
import edu.fpt.customflorist.responses.DeliveryHistory.DeliveryHistoryResponse;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long orderId;
    private Long userId;
    private String userName;
    private Long promotionId;
    private LocalDateTime orderDate;
    private String status;
    private BigDecimal totalPrice;
    private String phone;
    private String shippingAddress;
    private Boolean isActive;
    private String reason;
    private List<OrderItemResponse> orderItems;
    private List<DeliveryHistoryResponse> deliveryHistories;
}
