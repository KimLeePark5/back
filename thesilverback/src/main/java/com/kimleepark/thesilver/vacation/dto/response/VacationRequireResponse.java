package com.kimleepark.thesilver.vacation.dto.response;

import com.kimleepark.thesilver.vacation.domain.Require;
import com.kimleepark.thesilver.vacation.domain.VacationType;
import com.kimleepark.thesilver.vacation.domain.type.RequireStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class VacationRequireResponse {

    private final String vacationName;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final String reqContent;
    private final RequireStatusType ReqStatus;
    private final LocalDateTime reqDate;


    public static VacationRequireResponse from(final Require require) {
        return new VacationRequireResponse(
                require.getVacationType().getVacationName(),
                require.getStartDate(),
                require.getEndDate(),
                require.getReqContent(),
                require.getReqStatus(),
                require.getReqDate()
        );
    }
}
