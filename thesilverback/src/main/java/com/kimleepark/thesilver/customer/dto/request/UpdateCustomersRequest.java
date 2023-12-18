package com.kimleepark.thesilver.customer.dto.request;

import com.kimleepark.thesilver.customer.domain.type.CustomerGender;
import com.kimleepark.thesilver.customer.domain.type.CustomerStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class UpdateCustomersRequest {
    @NotBlank
    private final String name;
    @NotNull
    private final CustomerGender gender;
    @NotBlank
    private final String birthDate;
    @NotBlank
    private final String phone1;
    @NotBlank
    private final String phone2;
    @NotBlank
    private final String phone3;
    @NotBlank
    private final String postalCode;
    @NotBlank
    private final String primaryAddress;
    private final String detailAddress;
    private final String memo;
    private final String guardianName;
    private final String guardianRelationship;
    private final String guardianPhone1;
    private final String guardianPhone2;
    private final String guardianPhone3;
    @NotNull
    private final CustomerStatus customerStatus;

    public String getPhone() {
        return phone1 +"-"+ phone2 +"-"+ phone3;
    }
    public String getGuardianPhone() {
        return guardianPhone1 +"-"+ guardianPhone2 +"-"+ guardianPhone3;
    }

}
