package edu.fpt.customflorist.repositories;

import edu.fpt.customflorist.models.DeliveryHistory;
import edu.fpt.customflorist.models.Enums.DeliveryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface DeliveryHistoryRepository extends JpaRepository<DeliveryHistory, Long> {

    @Query("""
        SELECT DISTINCT d FROM DeliveryHistory d 
        JOIN d.statusHistories s 
        WHERE (:startDate IS NULL OR s.changedAt >= :startDate)
        AND (:endDate IS NULL OR s.changedAt <= :endDate)
        AND (:userId IS NULL OR d.user.userId = :userId)
        AND (:courierId IS NULL OR d.courier.userId = :courierId)
    """)
    Page<DeliveryHistory> findAllByFilters(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("userId") Long userId,
            @Param("courierId") Long courierId,
            Pageable pageable
    );

    @Query("""
    SELECT DISTINCT dh FROM DeliveryHistory dh
    WHERE dh.deliveryId IN (
        SELECT dsh.deliveryHistory.deliveryId
        FROM DeliveryStatusHistory dsh
        WHERE dsh.id IN (
            SELECT MAX(dsh2.id)
            FROM DeliveryStatusHistory dsh2
            WHERE dsh2.deliveryHistory.deliveryId = dsh.deliveryHistory.deliveryId
            GROUP BY dsh2.deliveryHistory.deliveryId
        )
        AND (:status IS NULL OR dsh.status = :status)
    )
    AND (:startDate IS NULL OR (
        SELECT dsh3.changedAt FROM DeliveryStatusHistory dsh3
        WHERE dsh3.deliveryHistory.deliveryId = dh.deliveryId
        AND dsh3.id = (
            SELECT MAX(dsh4.id) FROM DeliveryStatusHistory dsh4
            WHERE dsh4.deliveryHistory.deliveryId = dh.deliveryId
        )
    ) >= :startDate)
    AND (:endDate IS NULL OR (
        SELECT dsh5.changedAt FROM DeliveryStatusHistory dsh5
        WHERE dsh5.deliveryHistory.deliveryId = dh.deliveryId
        AND dsh5.id = (
            SELECT MAX(dsh6.id) FROM DeliveryStatusHistory dsh6
            WHERE dsh6.deliveryHistory.deliveryId = dh.deliveryId
        )
    ) <= :endDate)
    AND (:userId IS NULL OR dh.user.userId = :userId)
    AND (:courierId IS NULL OR dh.courier.userId = :courierId)
""")
    Page<DeliveryHistory> findAllByFilters(
            @Param("status") DeliveryStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("userId") Long userId,
            @Param("courierId") Long courierId,
            Pageable pageable
    );

    @Query("""
    SELECT DISTINCT dh FROM DeliveryHistory dh
    WHERE dh.deliveryId IN (
        SELECT dsh.deliveryHistory.deliveryId
        FROM DeliveryStatusHistory dsh
        WHERE dsh.id IN (
            SELECT MAX(dsh2.id)
            FROM DeliveryStatusHistory dsh2
            WHERE dsh2.deliveryHistory.deliveryId = dsh.deliveryHistory.deliveryId
            GROUP BY dsh2.deliveryHistory.deliveryId
        )
        AND (:status IS NULL OR dsh.status = :status)
    )
    AND dh.user.userId = :userId
    AND (:startDate IS NULL OR (
        SELECT dsh3.changedAt FROM DeliveryStatusHistory dsh3
        WHERE dsh3.deliveryHistory.deliveryId = dh.deliveryId
        AND dsh3.id = (
            SELECT MAX(dsh4.id) FROM DeliveryStatusHistory dsh4
            WHERE dsh4.deliveryHistory.deliveryId = dh.deliveryId
        )
    ) >= :startDate)
    AND (:endDate IS NULL OR (
        SELECT dsh5.changedAt FROM DeliveryStatusHistory dsh5
        WHERE dsh5.deliveryHistory.deliveryId = dh.deliveryId
        AND dsh5.id = (
            SELECT MAX(dsh6.id) FROM DeliveryStatusHistory dsh6
            WHERE dsh6.deliveryHistory.deliveryId = dh.deliveryId
        )
    ) <= :endDate)
""")
    Page<DeliveryHistory> findByUserUserIdAndStatusChangedAtBetween(
            @Param("userId") Long userId,
            @Param("status") DeliveryStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    @Query("""
    SELECT DISTINCT dh FROM DeliveryHistory dh
    WHERE dh.deliveryId IN (
        SELECT dsh.deliveryHistory.deliveryId
        FROM DeliveryStatusHistory dsh
        WHERE dsh.id IN (
            SELECT MAX(dsh2.id)
            FROM DeliveryStatusHistory dsh2
            WHERE dsh2.deliveryHistory.deliveryId = dsh.deliveryHistory.deliveryId
            GROUP BY dsh2.deliveryHistory.deliveryId
        )
        AND (:status IS NULL OR dsh.status = :status)
    )
    AND dh.courier.userId = :courierId
    AND (:startDate IS NULL OR (
        SELECT dsh3.changedAt FROM DeliveryStatusHistory dsh3
        WHERE dsh3.deliveryHistory.deliveryId = dh.deliveryId
        AND dsh3.id = (
            SELECT MAX(dsh4.id) FROM DeliveryStatusHistory dsh4
            WHERE dsh4.deliveryHistory.deliveryId = dh.deliveryId
        )
    ) >= :startDate)
    AND (:endDate IS NULL OR (
        SELECT dsh5.changedAt FROM DeliveryStatusHistory dsh5
        WHERE dsh5.deliveryHistory.deliveryId = dh.deliveryId
        AND dsh5.id = (
            SELECT MAX(dsh6.id) FROM DeliveryStatusHistory dsh6
            WHERE dsh6.deliveryHistory.deliveryId = dh.deliveryId
        )
    ) <= :endDate)
""")
    Page<DeliveryHistory> findByCourierUserIdAndStatusChangedAtBetween(
            @Param("courierId") Long courierId,
            @Param("status") DeliveryStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    @Query("""
    SELECT DISTINCT dh FROM DeliveryHistory dh
    WHERE dh.deliveryId IN (
        SELECT dsh.deliveryHistory.deliveryId
        FROM DeliveryStatusHistory dsh
        WHERE dsh.id IN (
            SELECT MAX(dsh2.id)
            FROM DeliveryStatusHistory dsh2
            WHERE dsh2.deliveryHistory.deliveryId = dsh.deliveryHistory.deliveryId
            GROUP BY dsh2.deliveryHistory.deliveryId
        )
        AND (:status IS NULL OR dsh.status = :status)
    )
    AND dh.isActive = true
    AND dh.user.userId = :userId
    AND (:startDate IS NULL OR (
        SELECT dsh3.changedAt FROM DeliveryStatusHistory dsh3
        WHERE dsh3.deliveryHistory.deliveryId = dh.deliveryId
        AND dsh3.id = (
            SELECT MAX(dsh4.id) FROM DeliveryStatusHistory dsh4
            WHERE dsh4.deliveryHistory.deliveryId = dh.deliveryId
        )
    ) >= :startDate)
    AND (:endDate IS NULL OR (
        SELECT dsh5.changedAt FROM DeliveryStatusHistory dsh5
        WHERE dsh5.deliveryHistory.deliveryId = dh.deliveryId
        AND dsh5.id = (
            SELECT MAX(dsh6.id) FROM DeliveryStatusHistory dsh6
            WHERE dsh6.deliveryHistory.deliveryId = dh.deliveryId
        )
    ) <= :endDate)
""")
    Page<DeliveryHistory> findActiveByUserUserIdAndStatusChangedAtBetween(
            @Param("userId") Long userId,
            @Param("status") DeliveryStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    @Query("""
    SELECT DISTINCT dh FROM DeliveryHistory dh
    WHERE dh.deliveryId IN (
        SELECT dsh.deliveryHistory.deliveryId
        FROM DeliveryStatusHistory dsh
        WHERE dsh.id IN (
            SELECT MAX(dsh2.id)
            FROM DeliveryStatusHistory dsh2
            WHERE dsh2.deliveryHistory.deliveryId = dsh.deliveryHistory.deliveryId
            GROUP BY dsh2.deliveryHistory.deliveryId
        )
        AND (:status IS NULL OR dsh.status = :status)
    )
    AND dh.isActive = true
    AND dh.courier.userId = :courierId
    AND (:startDate IS NULL OR (
        SELECT dsh3.changedAt FROM DeliveryStatusHistory dsh3
        WHERE dsh3.deliveryHistory.deliveryId = dh.deliveryId
        AND dsh3.id = (
            SELECT MAX(dsh4.id) FROM DeliveryStatusHistory dsh4
            WHERE dsh4.deliveryHistory.deliveryId = dh.deliveryId
        )
    ) >= :startDate)
    AND (:endDate IS NULL OR (
        SELECT dsh5.changedAt FROM DeliveryStatusHistory dsh5
        WHERE dsh5.deliveryHistory.deliveryId = dh.deliveryId
        AND dsh5.id = (
            SELECT MAX(dsh6.id) FROM DeliveryStatusHistory dsh6
            WHERE dsh6.deliveryHistory.deliveryId = dh.deliveryId
        )
    ) <= :endDate)
""")
    Page<DeliveryHistory> findActiveByCourierUserIdAndStatusChangedAtBetween(
            @Param("courierId") Long courierId,
            @Param("status") DeliveryStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

}
