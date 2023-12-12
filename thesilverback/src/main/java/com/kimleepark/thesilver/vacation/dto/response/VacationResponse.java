package com.kimleepark.thesilver.vacation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kimleepark.thesilver.vacation.domain.Vacation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class VacationResponse {

    private final String employeeName;
    private final Long occurVacation;
    private final Long useVacation;
    private final Long remainingVacation;


    public static VacationResponse from(final Vacation vacation, Long passedReqCount) {
        return new VacationResponse(
                vacation.getEmployee().getEmployeeName(),
                vacation.getOccurVacation(),
                passedReqCount, // reqStatus가 PASS 상태인 요청의 갯수
                vacation.getOccurVacation() - vacation.getUseVacation()
        );
    }

}
