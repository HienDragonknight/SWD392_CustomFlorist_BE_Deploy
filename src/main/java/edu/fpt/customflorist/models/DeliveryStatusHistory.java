package edu.fpt.customflorist.models;

import edu.fpt.customflorist.models.Enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "DeliveryStatusHistory")
public class DeliveryStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "delivery_id", nullable = false)
    private DeliveryHistory deliveryHistory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status;

    @Column(nullable = false)
    private LocalDateTime changedAt;

    @Column(nullable = true)
    private String note;

    @PrePersist
    public void prePersist() {
        this.changedAt = LocalDateTime.now();
    }
}

