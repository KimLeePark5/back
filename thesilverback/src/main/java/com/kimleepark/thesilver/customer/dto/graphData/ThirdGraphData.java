package com.kimleepark.thesilver.customer.dto.graphData;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface ThirdGraphData {

    String getMonth();
    Long getCountFemale();
    Long getCountMale();
}
