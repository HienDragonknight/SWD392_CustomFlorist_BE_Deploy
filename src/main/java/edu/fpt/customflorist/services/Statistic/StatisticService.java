package edu.fpt.customflorist.services.Statistic;

import edu.fpt.customflorist.repositories.BouquetRepository;
import edu.fpt.customflorist.repositories.FlowerRepository;
import edu.fpt.customflorist.repositories.OrderRepository;
import edu.fpt.customflorist.repositories.PaymentRepository;
import edu.fpt.customflorist.responses.Statistic.MonthlyStatisticsResponse;
import edu.fpt.customflorist.responses.Statistic.ProductStatistics;
import edu.fpt.customflorist.responses.Statistic.StatisticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticService implements IStatisticService {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final BouquetRepository bouquetRepository;
    private final FlowerRepository flowerRepository;

    public StatisticsResponse getMonthlyStatistics() {
        List<Object[]> orderStats = orderRepository.getOrdersStatisticsByMonthWithStatus();
        List<Object[]> paymentStats = paymentRepository.getPaymentsStatisticsByMonth();

        // Map để lưu dữ liệu thống kê theo (năm, tháng)
        Map<String, MonthlyStatisticsResponse> statsMap = new HashMap<>();

        // Xử lý dữ liệu Order
        for (Object[] row : orderStats) {
            int year = (int) row[0];
            int month = (int) row[1];
            String status = (String) row[2];
            long orderCount = (long) row[3];
            BigDecimal totalRevenue = (BigDecimal) row[4]; // Chỉ có giá trị nếu status = DELIVERED

            String key = year + "-" + month;
            MonthlyStatisticsResponse response = statsMap.computeIfAbsent(key,
                    k -> new MonthlyStatisticsResponse(year, month, BigDecimal.ZERO, BigDecimal.ZERO));

            response.addOrderCount(status, orderCount);

            // Nếu trạng thái là DELIVERED, cập nhật tổng doanh thu
            if ("DELIVERED".equals(status)) {
                response.setTotalOrderRevenue(totalRevenue);
            }
        }

        // Xử lý dữ liệu Payment
        for (Object[] row : paymentStats) {
            int year = (int) row[0];
            int month = (int) row[1];
            BigDecimal totalPaymentRevenue = (BigDecimal) row[2];

            String key = year + "-" + month;
            statsMap.computeIfAbsent(key, k -> new MonthlyStatisticsResponse(year, month, BigDecimal.ZERO, totalPaymentRevenue))
                    .setTotalPaymentRevenue(totalPaymentRevenue);
        }

        // Lấy số lượng active & inactive của Bouquet và Flower
        long activeBouquet = bouquetRepository.countActiveBouquets();
        long inactiveBouquet = bouquetRepository.countInactiveBouquets();
        long activeFlower = flowerRepository.countActiveFlowers();
        long inactiveFlower = flowerRepository.countInactiveFlowers();

        // Tạo object chứa thông tin sản phẩm
        ProductStatistics productStatistics = new ProductStatistics(activeBouquet, inactiveBouquet, activeFlower, inactiveFlower);

        return new StatisticsResponse(productStatistics, new ArrayList<>(statsMap.values()));
    }

}
