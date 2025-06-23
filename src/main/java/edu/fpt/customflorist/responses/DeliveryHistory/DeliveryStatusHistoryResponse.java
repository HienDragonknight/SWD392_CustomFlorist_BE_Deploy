package edu.fpt.customflorist.responses.DeliveryHistory;

import edu.fpt.customflorist.models.DeliveryStatusHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryStatusHistoryResponse {
    private Long id;
    private String status;
    private LocalDateTime changedAt;
    private String note;

    public static DeliveryStatusHistoryResponse fromEntity(DeliveryStatusHistory statusHistory) {
        return new DeliveryStatusHistoryResponse(
                statusHistory.getId(),
                statusHistory.getStatus().name(),
                statusHistory.getChangedAt(),
                statusHistory.getNote()
        );
    }

}

