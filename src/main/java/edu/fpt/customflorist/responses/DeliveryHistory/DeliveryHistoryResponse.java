package edu.fpt.customflorist.responses.DeliveryHistory;

import edu.fpt.customflorist.models.DeliveryHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryHistoryResponse {
    private Long deliveryId;
    private String deliveryCode;
    private Boolean isActive;
    private Long userId;
    private Long orderId;
    private Long courierId;
    private List<DeliveryStatusHistoryResponse> statusHistories;

    public static DeliveryHistoryResponse fromEntity(DeliveryHistory deliveryHistory) {
        return new DeliveryHistoryResponse(
                deliveryHistory.getDeliveryId(),
                deliveryHistory.getDeliveryCode(),
                deliveryHistory.getIsActive(),
                deliveryHistory.getUser().getUserId(),
                deliveryHistory.getOrder().getOrderId(),
                deliveryHistory.getCourier().getUserId(),
                deliveryHistory.getStatusHistories()
                        .stream()
                        .map(DeliveryStatusHistoryResponse::fromEntity)
                        .collect(Collectors.toList())
        );
    }
}
