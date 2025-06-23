package edu.fpt.customflorist.models;

import edu.fpt.customflorist.models.Enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "DeliveryHistory")
public class DeliveryHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "courier_id", nullable = false)
    private User courier;

    @Column(nullable = false, unique = true)
    private String deliveryCode;

    @OneToMany(mappedBy = "deliveryHistory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DeliveryStatusHistory> statusHistories = new ArrayList<>();

    @Column(nullable = false)
    private Boolean isActive;

    @PrePersist
    public void generateDeliveryCode() {
        if (this.deliveryCode == null || this.deliveryCode.isEmpty()) {
            this.deliveryCode = "DLV-" + UUID.randomUUID().toString().substring(0, 8);
        }
    }

    public boolean isCompletedOrCancelled() {
        if (statusHistories != null && !statusHistories.isEmpty()) {
            DeliveryStatus latestStatus = statusHistories.get(statusHistories.size() - 1).getStatus();
            return latestStatus == DeliveryStatus.DELIVERED || latestStatus == DeliveryStatus.CANCELLED;
        }
        return false;
    }

}
