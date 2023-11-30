package com.kimleepark.thesilver.customer.dto.response;

import com.kimleepark.thesilver.customer.domain.Customer;
import com.kimleepark.thesilver.customer.domain.type.CustomerGender;
import com.kimleepark.thesilver.customer.domain.type.CustomerStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class CustomerMainResponse {

    private final Long customerCode;
    private final String name;
    private final CustomerGender gender;
    private final String birthDate;
    private final String primaryAddress;
    private final CustomerStatus status;
    private final String phone;


    public static CustomerMainResponse from(final Customer customer) {
        return new CustomerMainResponse(
                customer.getCustomerCode(),
                customer.getName(),
                customer.getGender(),
                customer.getBirthDate(),
                customer.getPrimaryAddress(),
                customer.getStatus(),
                customer.getPhone()
        );
    }
}
