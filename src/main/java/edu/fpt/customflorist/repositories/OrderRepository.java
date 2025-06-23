package edu.fpt.customflorist.repositories;

import edu.fpt.customflorist.models.Enums.Status;
import edu.fpt.customflorist.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
        SELECT o FROM Order o
        LEFT JOIN FETCH o.deliveryHistories dh
        LEFT JOIN FETCH o.orderItems oi
        WHERE o.id = :orderId
    """)
    Optional<Order> findByIdWithDetails(@Param("orderId") Long orderId);

    @Query("""
    SELECT 
        YEAR(o.orderDate) AS year, 
        MONTH(o.orderDate) AS month, 
        CAST(o.status AS string) AS status,
        COUNT(o) AS orderCount,
        COALESCE(SUM(CASE WHEN o.status = 'DELIVERED' THEN o.totalPrice ELSE 0 END), 0) AS totalRevenue
    FROM Order o
    GROUP BY YEAR(o.orderDate), MONTH(o.orderDate), o.status
    ORDER BY year DESC, month DESC
""")
    List<Object[]> getOrdersStatisticsByMonthWithStatus();


    @Query("""
        SELECT o FROM Order o
        WHERE (:minOrderDate IS NULL OR o.orderDate >= :minOrderDate)
        AND (:maxOrderDate IS NULL OR o.orderDate <= :maxOrderDate)
        AND (:minPrice IS NULL OR o.totalPrice >= :minPrice)
        AND (:maxPrice IS NULL OR o.totalPrice <= :maxPrice)
        AND (:status IS NULL OR o.status = :status)
        AND (:userId IS NULL OR o.user.userId = :userId)
        AND (:userName IS NULL OR LOWER(o.user.name) LIKE LOWER(CONCAT('%', :userName, '%')))
                    AND (:phone IS NULL OR o.user.phone LIKE CONCAT('%', :phone, '%'))
    """)
    Page<Order> findAllByFilters(
            @Param("minOrderDate") LocalDateTime minOrderDate,
            @Param("maxOrderDate") LocalDateTime maxOrderDate,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("status") Status status,
            @Param("userId") Long userId,
            @Param("userName") String userName,
            @Param("phone") String phone,
            Pageable pageable
    );

    @Query("""
    SELECT o FROM Order o
    LEFT JOIN FETCH o.deliveryHistories dh
    LEFT JOIN FETCH o.orderItems oi
    WHERE (:minOrderDate IS NULL OR o.orderDate >= :minOrderDate)
    AND (:maxOrderDate IS NULL OR o.orderDate <= :maxOrderDate)
    AND (:minPrice IS NULL OR o.totalPrice >= :minPrice)
    AND (:maxPrice IS NULL OR o.totalPrice <= :maxPrice)
    AND (:status IS NULL OR o.status = :status)
    AND (:userId IS NULL OR o.user.userId = :userId)
    AND (:userName IS NULL OR LOWER(o.user.name) LIKE LOWER(CONCAT('%', :userName, '%')))
    AND (:phone IS NULL OR o.user.phone LIKE CONCAT('%', :phone, '%'))
""")
    Page<Order> findAllByFiltersWithDeliveries(
            @Param("minOrderDate") LocalDateTime minOrderDate,
            @Param("maxOrderDate") LocalDateTime maxOrderDate,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("status") Status status,
            @Param("userId") Long userId,
            @Param("userName") String userName,
            @Param("phone") String phone,
            Pageable pageable
    );

    @Query("""
        SELECT o FROM Order o
        LEFT JOIN FETCH o.deliveryHistories dh
        LEFT JOIN FETCH o.orderItems oi
        WHERE o.isActive = true
        AND o.user.userId = :userId
        AND (:minOrderDate IS NULL OR o.orderDate >= :minOrderDate)
        AND (:maxOrderDate IS NULL OR o.orderDate <= :maxOrderDate)
        AND (:minPrice IS NULL OR o.totalPrice >= :minPrice)
        AND (:maxPrice IS NULL OR o.totalPrice <= :maxPrice)
        AND (:status IS NULL OR o.status = :status)
    """)
    Page<Order> findActiveByFilters(
            @Param("userId") Long userId,
            @Param("minOrderDate") LocalDateTime minOrderDate,
            @Param("maxOrderDate") LocalDateTime maxOrderDate,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("status") Status status,
            Pageable pageable
    );

    @Query("""
        SELECT o FROM Order o
        LEFT JOIN FETCH o.deliveryHistories dh
        LEFT JOIN FETCH o.orderItems oi
        LEFT JOIN FETCH o.user u
        WHERE o.isActive = true
        AND (:minOrderDate IS NULL OR o.orderDate >= :minOrderDate)
        AND (:maxOrderDate IS NULL OR o.orderDate <= :maxOrderDate)
        AND (:minPrice IS NULL OR o.totalPrice >= :minPrice)
        AND (:maxPrice IS NULL OR o.totalPrice <= :maxPrice)
        AND (:status IS NULL OR o.status = :status)
        AND (:customerName IS NULL OR u.name LIKE %:customerName%)
    """)
    Page<Order> findActiveByFilters(
            @Param("minOrderDate") LocalDateTime minOrderDate,
            @Param("maxOrderDate") LocalDateTime maxOrderDate,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("status") Status status,
            @Param("customerName") String customerName,
            Pageable pageable
    );

}
