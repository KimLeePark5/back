package com.kimleepark.thesilver.customer.dto.request;

import com.kimleepark.thesilver.customer.domain.type.CustomerGender;
import com.kimleepark.thesilver.customer.domain.type.CustomerStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

@RequiredArgsConstructor
@Getter
public class CreateCustomersRequest {

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

    public String getPhone() {
        return phone1 +"-"+ phone2 +"-"+ phone3;
    }
    public String getGuardianPhone() {
        return guardianPhone1 +"-"+ guardianPhone2 +"-"+ guardianPhone3;
    }
    public String getParsedBirthDate() {
        String inputDate = birthDate;

        // 년, 월, 일을 각각 추출
        String year = "19" + inputDate.substring(0, 2);  // "19"를 추가
        String month = inputDate.substring(2, 4);
        String day = inputDate.substring(4, 6);

        // 최종적으로 "yyyy-MM-dd" 형식으로 조합
        return year + "-" + month + "-" + day;
    }
}
