package edu.fpt.customflorist.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "BouquetComposition")
public class BouquetComposition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bouquetCompositionId;

    @ManyToOne
    @JoinColumn(name = "bouquet_id", nullable = false)
    @JsonBackReference
    private Bouquet bouquet;

    @ManyToOne
    @JoinColumn(name = "flower_id", nullable = false)
    private Flower flower;

    @Column(nullable = true)
    private Integer minQuantity;

    @Column(nullable = true)
    private Integer maxQuantity;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Boolean isActive;

}
