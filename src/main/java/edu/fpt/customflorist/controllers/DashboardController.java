package edu.fpt.customflorist.controllers;

import edu.fpt.customflorist.responses.Dashboard.DashboardAmountResponse;
import edu.fpt.customflorist.responses.ResponseObject;
import edu.fpt.customflorist.services.Dashboard.IDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final IDashboardService dashboardService;

    @GetMapping("/amount")
    public ResponseObject getDashboardAmount() {
        java.util.List<DashboardAmountResponse> data = dashboardService.getDashboardAmount();
        return new ResponseObject("Lấy dữ liệu thành công", HttpStatus.OK, data);
    }
}
