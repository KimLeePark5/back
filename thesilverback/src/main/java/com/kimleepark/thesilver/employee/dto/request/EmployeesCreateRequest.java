package com.kimleepark.thesilver.employee.dto.request;

import com.kimleepark.thesilver.employee.type.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class EmployeesCreateRequest {

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
    @NotNull
    @Min(value = 99)
    private final Long teamCode;
}
