package com.kimleepark.thesilver.vacation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.jwt.CustomUser;
import com.kimleepark.thesilver.vacation.domain.Require;

import com.kimleepark.thesilver.vacation.domain.type.RequireStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class RequireStateAdminResponse {


    private final Long reqNo;
    private final String employeeName;
    private final String vacationName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime endDate;
    private final String reqContent;
    private final RequireStatusType ReqStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime reqDate;
    private final Long teamCode;
    private final String rank;




    public static RequireStateAdminResponse from(Require require) {

        return new RequireStateAdminResponse(
                require.getReqNo(),
                require.getEmployee().getEmployeeName(),
                require.getVacationType().getVacationName(),
                require.getStartDate(),
                require.getEndDate(),
                require.getReqContent(),
                require.getReqStatus(),
                require.getReqDate(),
                require.getEmployee().getTeam().getTeamCode(),
                require.getEmployee().getRank().getRankName()

        );
    }
}
