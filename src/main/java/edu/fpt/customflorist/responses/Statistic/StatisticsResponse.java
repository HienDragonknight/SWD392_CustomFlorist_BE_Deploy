package edu.fpt.customflorist.responses.Statistic;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StatisticsResponse {
    private ProductStatistics productStatistics;
    private List<MonthlyStatisticsResponse> monthlyStatistics;

    public StatisticsResponse(ProductStatistics productStatistics, List<MonthlyStatisticsResponse> monthlyStatistics) {
        this.productStatistics = productStatistics;
        this.monthlyStatistics = monthlyStatistics;
    }
}
