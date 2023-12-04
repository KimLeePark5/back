package com.kimleepark.thesilver.account.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
public class ResetPasswordRequest {
    @NotBlank
    private final String employeeNumber;
    @NotBlank
    private final String employeeEmail;
}
