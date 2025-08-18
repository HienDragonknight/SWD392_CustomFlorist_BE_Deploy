package edu.fpt.customflorist.models;

import edu.fpt.customflorist.models.Enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "`Order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "promotion_id", nullable = true)
    private Promotion promotion;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column(nullable = true)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Column(nullable = false, length = 255)
    private String shippingAddress;

    @Column(nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<Payment> payments = new HashSet<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OrderItem> orderItems = new HashSet<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<DeliveryHistory> deliveryHistories = new HashSet<>();

}

