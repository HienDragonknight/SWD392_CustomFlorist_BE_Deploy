package edu.fpt.customflorist.responses.Order;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderBouquetFlowerResponse {
    private Long flowerId;
    private String flowerName;
    private Integer quantity;
}
