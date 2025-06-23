package edu.fpt.customflorist.responses.Statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class MonthlyStatisticsResponse {
    private int year;
    private int month;
    private BigDecimal totalOrderRevenue;
    private BigDecimal totalPaymentRevenue;
    private Map<String, Long> orderCountsByStatus = new HashMap<>();

    public MonthlyStatisticsResponse(int year, int month, BigDecimal totalOrderRevenue, BigDecimal totalPaymentRevenue) {
        this.year = year;
        this.month = month;
        this.totalOrderRevenue = totalOrderRevenue;
        this.totalPaymentRevenue = totalPaymentRevenue;
        this.orderCountsByStatus = new HashMap<>();
    }

    public void addOrderCount(String status, long count) {
        orderCountsByStatus.put(status, count);
    }
}

