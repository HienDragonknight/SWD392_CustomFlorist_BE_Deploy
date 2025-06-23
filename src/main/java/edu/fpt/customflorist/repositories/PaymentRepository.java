package edu.fpt.customflorist.repositories;

import edu.fpt.customflorist.models.Enums.PaymentStatus;
import edu.fpt.customflorist.models.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("""
        SELECT 
            YEAR(p.paymentDate) AS year, 
            MONTH(p.paymentDate) AS month, 
            COALESCE(SUM(p.amount), 0) AS totalRevenue
        FROM Payment p
        WHERE p.status = 'COMPLETED'
        GROUP BY YEAR(p.paymentDate), MONTH(p.paymentDate)
        ORDER BY year DESC, month DESC
    """)
    List<Object[]> getPaymentsStatisticsByMonth();

    @Query("SELECT p FROM Payment p WHERE p.order.orderId = :orderId")
    Optional<Payment> findByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Payment p WHERE p.order.orderId = :orderId")
    boolean existsByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT p FROM Payment p WHERE " +
            "(:status IS NULL OR p.status = :status) AND " +
            "(:fromDate IS NULL OR p.paymentDate >= :fromDate) AND " +
            "(:toDate IS NULL OR p.paymentDate <= :toDate) AND " +
            "(:minAmount IS NULL OR p.amount >= :minAmount) AND " +
            "(:maxAmount IS NULL OR p.amount <= :maxAmount)")
    Page<Payment> findAllWithFilters(
            @Param("status") PaymentStatus status,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            @Param("minAmount") BigDecimal minAmount,
            @Param("maxAmount") BigDecimal maxAmount,
            Pageable pageable);
}
