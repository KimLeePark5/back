package com.kimleepark.thesilver.attend.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class ResponseModifiedAttend {
    private final int employeeCode;
    private final LocalDateTime modifiedAt;
    private final LocalTime beforeEntertime;
    private final LocalTime afterEntertime;
    private final LocalTime beforeLeavetime;
    private final LocalTime afterLeavetime;
    private final String note;
    private final String type;
}
