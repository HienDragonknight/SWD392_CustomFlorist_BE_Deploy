package edu.fpt.customflorist.controllers;

import edu.fpt.customflorist.responses.ResponseObject;
import edu.fpt.customflorist.responses.Statistic.MonthlyStatisticsResponse;
import edu.fpt.customflorist.responses.Statistic.StatisticsResponse;
import edu.fpt.customflorist.services.Statistic.IStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/api/v1/statistic")
@CrossOrigin(origins = {"*", "http://localhost:3000", "https://yourflorist.vercel.app"})
@RequiredArgsConstructor
public class StatisticController {
    private final IStatisticService statisticService;

    @GetMapping("/monthly-statistics")
    public ResponseEntity<ResponseObject> getMonthlyStatistics() {
        try {
            StatisticsResponse statistics = statisticService.getMonthlyStatistics();
            return ResponseEntity.ok(
                    new ResponseObject("Success", HttpStatus.OK, statistics)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject("System error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null));
        }
    }


}
