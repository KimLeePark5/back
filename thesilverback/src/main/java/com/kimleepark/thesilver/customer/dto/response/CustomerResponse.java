package com.kimleepark.thesilver.customer.dto.response;

import com.kimleepark.thesilver.customer.domain.Customer;
import com.kimleepark.thesilver.customer.domain.type.CustomerGender;
import com.kimleepark.thesilver.customer.domain.type.CustomerStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

import static com.kimleepark.thesilver.customer.domain.type.CustomerStatus.ACTIVE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@RequiredArgsConstructor
public class CustomerResponse {

    private final Long customerCode;
    private final String name;
    private final CustomerGender gender;
    private final String birthDate;
    private final String phone;
    private final String postalCode;
    private final String primaryAddress;
    private final String detailAddress;
    private final String memo;
    private final String guardianName;
    private final String guardianRelationship;
    private final String guardianPhone;
    private final LocalDateTime registDate;
    private final LocalDateTime modifyDate;
    private final CustomerStatus status;
    private final Long employeeCode;

    public static CustomerResponse from(final Customer customer) {
        return new CustomerResponse(
                customer.getCustomerCode(),
                customer.getName(),
                customer.getGender(),
                customer.getBirthDate(),
                customer.getPhone(),
                customer.getPostalCode(),
                customer.getPrimaryAddress(),
                customer.getDetailAddress(),
                customer.getMemo(),
                customer.getGuardianName(),
                customer.getGuardianRelationship(),
                customer.getGuardianPhone(),
                customer.getRegistDate(),
                customer.getModifyDate(),
                customer.getStatus(),
                customer.getEmployeeCode()
        );
    }
}
