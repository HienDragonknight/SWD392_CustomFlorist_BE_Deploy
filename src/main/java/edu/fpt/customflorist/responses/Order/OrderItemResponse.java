package edu.fpt.customflorist.responses.Order;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {
    private Long orderItemId;
    private Long bouquetId;
    private String bouquetName;
    private Integer quantity;
    private BigDecimal subTotal;
    private Boolean isActive;
    private List<OrderBouquetFlowerResponse> bouquetFlowers;
}
