package edu.fpt.customflorist.responses.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DashboardAmountResponse {
    private String month;
    private BigDecimal amount;
}

