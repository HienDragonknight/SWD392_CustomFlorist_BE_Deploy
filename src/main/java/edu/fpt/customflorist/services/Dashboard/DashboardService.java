package edu.fpt.customflorist.services.Dashboard;

import edu.fpt.customflorist.repositories.PaymentRepository;
import edu.fpt.customflorist.responses.Dashboard.DashboardAmountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService implements IDashboardService {
    private final PaymentRepository paymentRepository;

    @Override
    public List<DashboardAmountResponse> getDashboardAmount() {
        LocalDateTime startDate = LocalDate.now()
                .withDayOfMonth(1)
                .minusMonths(11)
                .atStartOfDay();

        List<Object[]> amounts = paymentRepository.getDashboardAmounts(startDate);

        return amounts.stream()
                .map(r -> new DashboardAmountResponse(
                        r[0] + "/" + r[1],
                        new BigDecimal(r[2].toString())
                ))
                .toList();
    }
}
