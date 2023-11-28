package com.kimleepark.thesilver.employee.dto;

import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.employee.Rank;
import com.kimleepark.thesilver.employee.type.GenderType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class CustomerEmployeeResponse {

    private final Long employeeCode;
    private final String rank;
    private final String employeeName;
    private final GenderType gender;
    private final String registrationNumber;
    private final String employeePhone;
    private final LocalDateTime joinDate;
    private final LocalDateTime leaveDate;
    private final String team;

    public static CustomerEmployeeResponse from(final Employee employee){
        return new CustomerEmployeeResponse(
                employee.getEmployeeCode(),
                employee.getRank().getRankName(),
                employee.getEmployeeName(),
                employee.getGender(),
                employee.getRegistrationNumber(),
                employee.getEmployeePhone(),
                employee.getJoinDate(),
                employee.getLeaveDate(),
                employee.getTeam().getTeamName()
        );
    }
}
