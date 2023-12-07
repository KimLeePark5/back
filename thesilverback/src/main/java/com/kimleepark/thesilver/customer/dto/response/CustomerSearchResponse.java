package com.kimleepark.thesilver.customer.dto.response;

import com.kimleepark.thesilver.customer.domain.type.CustomerGender;
import com.kimleepark.thesilver.customer.domain.type.CustomerStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class CustomerSearchResponse {

    private final Long customerCode;
    private final String name;
    private final String gender;
    private final String birthDate;
    private final String primaryAddress;
    private final String status;
    private final String phone;

    @QueryProjection
    public CustomerSearchResponse(Long customerCode, String name, CustomerGender gender, String birthDate, String primaryAddress, CustomerStatus status, String phone) {
        String genderString = (gender == CustomerGender.FEMALE) ? "여성" : "남성";
        String statusString = (status == CustomerStatus.ACTIVE) ? "등록" : "해지";

        this.customerCode = customerCode;
        this.name = name;
        this.gender = genderString;
        this.birthDate = birthDate;
        this.primaryAddress = primaryAddress;
        this.status = statusString;
        this.phone = phone;
    }
}
