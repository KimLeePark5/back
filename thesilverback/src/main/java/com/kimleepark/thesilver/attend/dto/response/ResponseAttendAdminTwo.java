package com.kimleepark.thesilver.attend.dto.response;

import com.kimleepark.thesilver.employee.Employee;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
@ToString
public class ResponseAttendAdminTwo {


    private final Long empCode;
    private final String empRank;
    private final String team;
    private final String empName;
    private final List<ResponseAttend> attendList;
    private final int absentCount;
    private final int lateCount;
    private final int leaveEarlyCount;
    private final int vacCount;
    private final int AttendTime;


    public static ResponseAttendAdminTwo of(Employee emp, LocalDate start, LocalDate end) {
        List<ResponseAttend> responseAttends = emp.getAttendList().stream().filter(at->at.getAttendDate().isAfter(start) && at.getAttendDate().isBefore(end)).map(at -> ResponseAttend.from(at,emp.getEmployeeCode())).collect(Collectors.toList());
        ResponseAttendType type = ResponseAttendType.getAttendTypeCount(responseAttends,emp.getEmployeeCode());
        return new ResponseAttendAdminTwo(
                emp.getEmployeeCode(),
                emp.getRank().getRankName(),
                emp.getTeam().getTeamName(),
                emp.getEmployeeName(),
                responseAttends,
                type.getAbsentCount(),
                type.getLateCount(),
                type.getLeaveEarlyCount(),
                type.getVacationCount(),
                type.getTotalAttendTime()
        );
    }
}
