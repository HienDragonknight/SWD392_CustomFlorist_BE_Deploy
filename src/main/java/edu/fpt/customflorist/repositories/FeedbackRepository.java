package edu.fpt.customflorist.repositories;

import edu.fpt.customflorist.models.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query("""
        SELECT f FROM Feedback f
        WHERE (:startDate IS NULL OR f.createdAt >= :startDate)
        AND (:endDate IS NULL OR f.createdAt <= :endDate)
        AND (:userId IS NULL OR f.user.userId = :userId)
    """)
    Page<Feedback> findAllByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Query("SELECT f FROM Feedback f WHERE f.bouquet.bouquetId = :bouquetId "
            + "AND (:startDate IS NULL OR f.createdAt >= :startDate) "
            + "AND (:endDate IS NULL OR f.createdAt <= :endDate)")
    Page<Feedback> findByBouquetIdAndDateRange(
            @Param("bouquetId") Long bouquetId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    @Query("SELECT f FROM Feedback f WHERE f.user.userId = :userId "
            + "AND (:startDate IS NULL OR f.createdAt >= :startDate) "
            + "AND (:endDate IS NULL OR f.createdAt <= :endDate)")
    Page<Feedback> findByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    @Query("SELECT f FROM Feedback f WHERE f.bouquet.bouquetId = :bouquetId "
            + "AND f.isActive = true "
            + "AND (:startDate IS NULL OR f.createdAt >= :startDate) "
            + "AND (:endDate IS NULL OR f.createdAt <= :endDate)")
    Page<Feedback> findIsActiveByBouquetIdAndDateRange(
            @Param("bouquetId") Long bouquetId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    @Query("SELECT f FROM Feedback f WHERE f.user.userId = :userId "
            + "AND f.isActive = true "
            + "AND (:startDate IS NULL OR f.createdAt >= :startDate) "
            + "AND (:endDate IS NULL OR f.createdAt <= :endDate)")
    Page<Feedback> findIsActiveByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

}

