package com.kimleepark.thesilver.customer.dto.graphData;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ThirdGraphData {

    private final String month;
    private final int countFemale;
    private final int countMale;
}
