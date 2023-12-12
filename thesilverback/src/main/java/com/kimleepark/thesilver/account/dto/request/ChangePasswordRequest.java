package com.kimleepark.thesilver.account.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
public class ChangePasswordRequest {
    @NotBlank
    private final String currentPassword;
    @NotBlank
    private final String newPassword;
}