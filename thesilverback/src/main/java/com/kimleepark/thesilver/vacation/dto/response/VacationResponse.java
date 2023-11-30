package com.kimleepark.thesilver.vacation.dto.response;

import com.kimleepark.thesilver.vacation.domain.Vacation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class VacationResponse {

    private final Long vacationCode;
    private final LocalDateTime occurYear;
    private final Long occurVacation;
    private final Long useVacation;


    public static VacationResponse from(final Vacation vacation) {
        return new VacationResponse(
                vacation.getVacationCode(),
                vacation.getOccurYear(),
                vacation.getOccurVacation(),
                vacation.getUseVacation()
        );
    }
}
