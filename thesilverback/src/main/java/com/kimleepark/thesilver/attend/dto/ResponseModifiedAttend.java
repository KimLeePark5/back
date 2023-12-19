package com.kimleepark.thesilver.attend.dto;

import com.kimleepark.thesilver.attend.domain.ModifiedAttend;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class ResponseModifiedAttend {
    private final int attendNo;
    private final String employeeName;
    private final LocalDateTime modifiedAt;

    private final LocalTime beforeEntertime;
    private final LocalTime afterEntertime;
    private final LocalTime beforeLeavetime;
    private final LocalTime afterLeavetime;
    private final String note;
    private final String type;


    public static ResponseModifiedAttend from(ModifiedAttend history) {
        return new ResponseModifiedAttend(
                history.getAttendNo(),
                history.getEmployeeCode().getEmployeeName(),
                history.getModifiedAt(),
                history.getBeforeEntertime(),
                history.getAfterEntertime(),
                history.getBeforeLeavetime(),
                history.getAfterLeavetime(),
                history.getNote(),
                history.getType()
        );
    }
}
