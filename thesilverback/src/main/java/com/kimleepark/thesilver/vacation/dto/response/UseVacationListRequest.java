package com.kimleepark.thesilver.vacation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kimleepark.thesilver.vacation.domain.Require;
import com.kimleepark.thesilver.vacation.domain.type.RequireStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UseVacationListRequest {

    private final String vacationName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime endDate;
    private final String reqContent;
    private final RequireStatusType ReqStatus;

   public static UseVacationListRequest from(final Require require) {
       return new UseVacationListRequest(
               require.getVacationType().getVacationName(),
               require.getStartDate(),
               require.getEndDate(),
               require.getReqContent(),
               require.getReqStatus()
       );
   }
}
