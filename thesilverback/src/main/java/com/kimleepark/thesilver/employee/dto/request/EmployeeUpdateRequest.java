package com.kimleepark.thesilver.employee.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@Getter
public class EmployeeUpdateRequest {

    @NotNull
    private final String employeeEmail;
    @NotNull
    private final String employeePhone;
    private final String employeeAddress;
}
