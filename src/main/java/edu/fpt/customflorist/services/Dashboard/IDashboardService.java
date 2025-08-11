package edu.fpt.customflorist.services.Dashboard;

import edu.fpt.customflorist.responses.Dashboard.DashboardAmountResponse;

import java.util.List;

public interface IDashboardService {
    List<DashboardAmountResponse> getDashboardAmount();
}
