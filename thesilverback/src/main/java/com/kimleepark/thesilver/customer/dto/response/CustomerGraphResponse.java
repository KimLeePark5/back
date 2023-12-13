package com.kimleepark.thesilver.customer.dto.response;

import com.kimleepark.thesilver.customer.dto.graphData.FirstGraphData;
import com.kimleepark.thesilver.customer.dto.graphData.SecondGraphData;
import com.kimleepark.thesilver.customer.dto.graphData.ThirdGraphData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class CustomerGraphResponse {
    private final Map<String,List<FirstGraphData>> firstGraphData;
    private final List<SecondGraphData> secondGraphData;
    private final List<ThirdGraphData> thirdGraphData;
}
