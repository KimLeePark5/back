package com.kimleepark.thesilver.customer.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class LicenseCustomerResponse {

    private final List<LicensesResponse> licenses;
    private final CustomerResponse customer;

    public static LicenseCustomerResponse of(Page<LicensesResponse> licenses, CustomerResponse customer) {
        return new LicenseCustomerResponse(licenses.getContent(), customer);
    }
}
