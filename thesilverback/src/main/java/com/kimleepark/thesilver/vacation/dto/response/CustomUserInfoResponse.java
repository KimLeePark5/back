package com.kimleepark.thesilver.vacation.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomUserInfoResponse {
    private final String employeeName;
    private final String authorities;
}
