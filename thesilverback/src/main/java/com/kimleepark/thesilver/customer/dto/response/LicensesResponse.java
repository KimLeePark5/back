package com.kimleepark.thesilver.customer.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kimleepark.thesilver.customer.domain.License;
import com.kimleepark.thesilver.customer.domain.type.LicenseStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class LicensesResponse {
    private final Long licenseCode;
    private final Long customerCode;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate endDate;
    private final LicenseStatus status;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime registDate;

    public static LicensesResponse from(License license) {
        return new LicensesResponse(
                license.getLicenseCode(),
                license.getCustomer().getCustomerCode(),
                license.getStartDate(),
                license.getEndDate(),
                license.getStatus(),
                license.getRegistDate()
                );
    }


}
