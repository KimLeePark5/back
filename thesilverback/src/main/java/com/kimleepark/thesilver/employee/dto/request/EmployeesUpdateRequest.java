package com.kimleepark.thesilver.employee.dto.request;

import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.employee.Rank;
import com.kimleepark.thesilver.employee.Team;
import com.kimleepark.thesilver.employee.type.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class EmployeesUpdateRequest {

    private final String employeePicture;
    @NotNull
    private final Long rankCode;
    @NotBlank
    private final String employeeName;

    private final String employeeEmail;

    private final GenderType gender;

    private final DisabilityType disability;

    private final MarriageType marriage;

    private final PatriotsType patriots;

    private final EmploymentType employmentType;

    private final WorkingType workingStatus;
    @NotNull
    private final LeaveType leaveType;
    @NotBlank
    private final String registrationNumber;
    @NotBlank
    private final String employeePhone;

    private final String employeeAddress;
    @NotNull
    private final LocalDateTime joinDate;

    private final LocalDateTime leaveDate;

    private final String leaveReason;
    @NotNull
    @Min(value = 99)
    private final Long teamCode;
}
