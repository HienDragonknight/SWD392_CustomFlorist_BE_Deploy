package edu.fpt.customflorist.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_bouquet_flower")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderBouquetFlower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderBouquetFlowerId;

    @ManyToOne
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @ManyToOne
    @JoinColumn(name = "flower_id", nullable = false)
    private Flower flower;

    @Column(nullable = false)
    private Integer quantity;
}
