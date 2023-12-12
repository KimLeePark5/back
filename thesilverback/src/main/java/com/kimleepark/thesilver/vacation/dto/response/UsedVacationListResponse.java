package com.kimleepark.thesilver.vacation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kimleepark.thesilver.vacation.domain.Require;
import com.kimleepark.thesilver.vacation.domain.type.RequireStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UsedVacationListResponse {

    private final String vacationName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime endDate;
    private final String reqContent;
    private final RequireStatusType ReqStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime reqDate;




    public static UsedVacationListResponse from(Require require) {
        return new UsedVacationListResponse(
                require.getVacationType().getVacationName(),
                require.getStartDate(),
                require.getEndDate(),
                require.getReqContent(),
                require.getReqStatus(),
                require.getReqDate()
        );
    }
}
