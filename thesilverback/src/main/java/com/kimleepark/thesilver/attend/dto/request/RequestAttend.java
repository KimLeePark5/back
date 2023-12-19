package com.kimleepark.thesilver.attend.dto.request;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalTime;

@RequiredArgsConstructor
@Getter
@ToString
public class RequestAttend {
    private final LocalTime enterTime;
    private final LocalTime leaveTime;

    private final String note;
    private final String type;
}
