package com.kimleepark.thesilver.attend.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class ResponseTypeAndAttend {
    private final List<ResponseAttend> responseAttend;
    private final ResponseAttendType responseAttendType;

public static ResponseTypeAndAttend of(List<ResponseAttend> responseAttend, ResponseAttendType responseAttendType){
    return new ResponseTypeAndAttend(responseAttend, responseAttendType);
}
}
