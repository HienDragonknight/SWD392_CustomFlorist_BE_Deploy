package edu.fpt.customflorist.responses.Statistic;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductStatistics {
    private long activeBouquetCount;
    private long inactiveBouquetCount;
    private long activeFlowerCount;
    private long inactiveFlowerCount;

    public ProductStatistics(long activeBouquetCount, long inactiveBouquetCount, long activeFlowerCount, long inactiveFlowerCount) {
        this.activeBouquetCount = activeBouquetCount;
        this.inactiveBouquetCount = inactiveBouquetCount;
        this.activeFlowerCount = activeFlowerCount;
        this.inactiveFlowerCount = inactiveFlowerCount;
    }
}

