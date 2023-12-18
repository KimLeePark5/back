package com.kimleepark.thesilver.customer.dto.graphData;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface FirstGraphData {
    Long getYear();
    Long getMonth();
    Long getCumulativeCount();
}
