package edu.fpt.customflorist.services.DeliveryHistory;

import edu.fpt.customflorist.dtos.DeliveryHistory.DeleteDeliveryHistoryDTO;
import edu.fpt.customflorist.dtos.DeliveryHistory.DeliveryHistoryDTO;
import edu.fpt.customflorist.dtos.DeliveryHistory.UpdateDeliveryHistoryDTO;
import edu.fpt.customflorist.models.DeliveryHistory;
import edu.fpt.customflorist.models.DeliveryStatusHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface IDeliveryHistoryService {
    DeliveryHistory createDeliveryHistory(DeliveryHistoryDTO deliveryHistoryDTO) throws Exception;
    DeliveryHistory getDeliveryHistoryById(Long deliveryId) throws Exception;
    DeliveryStatusHistory updateDeliveryHistory(Long deliveryId, UpdateDeliveryHistoryDTO updateDeliveryHistoryDTO) throws Exception;
    void deleteDeliveryHistory(Long deliveryId, DeleteDeliveryHistoryDTO deleteDeliveryHistoryDTO) throws Exception;

    Page<DeliveryHistory> getAllDeliveryHistories(LocalDateTime startDate, LocalDateTime endDate,  Long userId, Long courierId, String status, Pageable pageable);
    Page<DeliveryHistory> getAllActiveByUserId(Long userId, LocalDateTime startDate, LocalDateTime endDate, String status, Pageable pageable);
    Page<DeliveryHistory> getAllActiveByCourierId(Long courierId, LocalDateTime startDate, LocalDateTime endDate, String status, Pageable pageable);
    Page<DeliveryHistory> getAllByUserId(Long userId, LocalDateTime startDate, LocalDateTime endDate, String status, Pageable pageable);
    Page<DeliveryHistory> getAllByCourierId(Long courierId, LocalDateTime startDate, LocalDateTime endDate, String status, Pageable pageable);
}

