package com.kimleepark.thesilver.employee.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kimleepark.thesilver.account.domain.Account;
import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.employee.Rank;
import com.kimleepark.thesilver.employee.Team;
import com.kimleepark.thesilver.employee.type.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class CustomerEmployeesResponse {

    private final Long employeeCode;
    private final String employeePicture;
    private final Rank rank;
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
    @JsonFormat(pattern = "yyyy.MM.dd")
    private final LocalDate joinDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDateTime leaveDate;
    private final String leaveReason;
    private final Team team;
    private final LeaveType leaveType;
    private final List leaveHistoryList;
    private final List rankHistory;
    private final String registrationNumberFull;
    private final String employeeEmail;
    private final Account account;


    public static CustomerEmployeesResponse from(final Employee employee){
        System.out.println(employee.getRankHistoryList());

//            String registrationNumber = employee.getRegistrationNumber().substring(0, 2);
//            String registrationNumber2 = employee.getRegistrationNumber().substring(2, 4);
//            String registrationNumber3 = employee.getRegistrationNumber().substring(4, 6);

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
                    map.put("rankNum", String.valueOf(rHistory.getUpdateRankcode()));
                    map.put("beforeRank", rHistory.getBeforeRank().getRankName());
                    map.put("afterRank", rHistory.getAfterRank().getRankName());
                    map.put("updateDate", String.valueOf(rHistory.getUpdateDate()));
                    map.put("upDown", rHistory.getUpDown().toString());
                    map.put("updateNote", rHistory.getUpdateNote().toString());
                    return map;
                }).collect(Collectors.toList());



        return new CustomerEmployeesResponse(
                employee.getEmployeeCode(),
                employee.getEmployeePicture(),
                employee.getRank(),
                employee.getEmployeeName(),
                employee.getGender(),
                employee.getDisability(),
                employee.getMarriage(),
                employee.getPatriots(),
                employee.getEmploymentType(),
                employee.getWorkingStatus(),
                employee.getRegistrationNumber(),
//                registrationNumber.concat(".").concat(registrationNumber2).concat(".").concat(registrationNumber3),
                employee.getEmployeePhone(),
                employee.getEmployeeAddress(),
                employee.getJoinDate(),
                employee.getLeaveDate(),
                employee.getLeaveReason(),
                employee.getTeam(),
                employee.getLeaveType(),
                leaveHistory,
                rankHistory,
                employee.getRegistrationNumber(),
                employee.getEmployeeEmail(),
                employee.getAccount()
        );
    }

//    public static CustomerEmployeeResponse getCustomerEmployee(Long employeeCode) {
//    }
}
