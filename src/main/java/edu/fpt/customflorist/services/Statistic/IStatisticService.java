package edu.fpt.customflorist.services.Statistic;

import edu.fpt.customflorist.responses.Statistic.StatisticsResponse;

import java.util.List;

public interface IStatisticService {
    StatisticsResponse getMonthlyStatistics();
}
