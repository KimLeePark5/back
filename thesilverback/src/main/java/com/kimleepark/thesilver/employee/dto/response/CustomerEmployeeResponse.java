package com.kimleepark.thesilver.employee.dto.response;

import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.employee.type.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class CustomerEmployeeResponse {

    private final Long employeeCode;
    private final String employeePicture;
    private final String rank;
    private final String employeeName;
    private final GenderType gender;
    private final DisabilityType disability;
    private final MarriageType marriage;
    private final PatriotsType patriots;
    private final EmploymentType employmentType;
    private final WorkingType workingStatus;
    private final String registrationNumber;
    private final String employeePhone;
    private final String employeeAddress;
    private final LocalDateTime joinDate;
    private final LocalDateTime leaveDate;
    private final String leaveReason;
    private final String team;
    private final LeaveType leaveType;
    private final List leaveHistoryList;
    private final List rankHistory;


    public static CustomerEmployeeResponse from(final Employee employee){
        System.out.println(employee.getRankHistoryList());

        List leaveHistory = employee.getLeaveHistoryList().stream().map(
                lHistory -> {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("startDate", String.valueOf(lHistory.getStartDate()));
                    map.put("backDate", String.valueOf(lHistory.getBackDate()));
                    return map;
                }).collect(Collectors.toList());

        List rankHistory = employee.getRankHistoryList().stream().map(
                rHistory -> {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("beforeRank", rHistory.getBeforeRank().getRankName());
                    map.put("afterRank", rHistory.getAfterRank().getRankName());
                    map.put("updateDate", String.valueOf(rHistory.getUpdateDate()));
                    map.put("upDown", rHistory.getUpDown().toString());
                    map.put("updateNote", rHistory.getUpdateNote().toString());
                    return map;
                }).collect(Collectors.toList());



        return new CustomerEmployeeResponse(
                employee.getEmployeeCode(),
                employee.getEmployeePicture(),
                employee.getRank().getRankName(),
                employee.getEmployeeName(),
                employee.getGender(),
                employee.getDisability(),
                employee.getMarriage(),
                employee.getPatriots(),
                employee.getEmploymentType(),
                employee.getWorkingStatus(),
                employee.getRegistrationNumber(),
                employee.getEmployeePhone(),
                employee.getEmployeeAddress(),
                employee.getJoinDate(),
                employee.getLeaveDate(),
                employee.getLeaveReason(),
                employee.getTeam().getTeamName(),
                employee.getLeaveType(),
                leaveHistory,
                rankHistory
        );
    }

//    public static CustomerEmployeeResponse getCustomerEmployee(Long employeeCode) {
//    }
}
