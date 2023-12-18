package com.kimleepark.thesilver.customer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kimleepark.thesilver.board.journal.domain.Journal;
import com.kimleepark.thesilver.customer.domain.type.CustomerGender;
import com.kimleepark.thesilver.customer.domain.type.CustomerStatus;
import com.kimleepark.thesilver.customer.dto.request.CreateCustomersRequest;
import com.kimleepark.thesilver.customer.dto.request.UpdateCustomersRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.time.LocalDateTime;

import static com.kimleepark.thesilver.customer.domain.type.CustomerStatus.ACTIVE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_customer")
@Getter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Customer {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long customerCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(value = STRING)
    private CustomerGender gender;

    @Column(nullable = false)
    private String birthDate;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String primaryAddress;

    @Column
    private String detailAddress;

    @Column
    private String memo;

    @Column
    private String guardianName;

    @Column
    private String guardianRelationship;

    @Column
    private String guardianPhone;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime registDate;

    @LastModifiedDate
    @Column
    private LocalDateTime modifyDate;

    @Column(nullable = false)
    @Enumerated(value = STRING)
    private CustomerStatus status = ACTIVE;

    @Column(nullable = false)
    private Long employeeCode;


    public Customer(
            final String name,
            final CustomerGender gender,
            final String birthDate,
            final String phone,
            final String postalCode,
            final String primaryAddress,
            final String detailAddress,
            final String memo,
            final String guardianName,
            final String guardianRelationship,
            final String guardianPhone,
            final Long employeeCode
    ) {
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.phone = phone;
        this.postalCode = postalCode;
        this.primaryAddress = primaryAddress;
        this.detailAddress = detailAddress;
        this.memo = memo;
        this.guardianName = guardianName;
        this.guardianRelationship = guardianRelationship;
        this.guardianPhone = guardianPhone;
        this.employeeCode = employeeCode;
    }

    public static Customer of(Long employeeCode,CreateCustomersRequest createCustomersRequest) {
        return new Customer(
                createCustomersRequest.getName(),
                createCustomersRequest.getGender(),
                createCustomersRequest.getParsedBirthDate(),
                createCustomersRequest.getPhone(),
                createCustomersRequest.getPostalCode(),
                createCustomersRequest.getPrimaryAddress(),
                createCustomersRequest.getDetailAddress(),
                createCustomersRequest.getMemo(),
                createCustomersRequest.getGuardianName(),
                createCustomersRequest.getGuardianRelationship(),
                createCustomersRequest.getGuardianPhone(),
                employeeCode
        );
    }

    public void update(Long employeeCode, UpdateCustomersRequest updateCustomersRequest) {
        this.name = updateCustomersRequest.getName();
        this.gender = updateCustomersRequest.getGender();
//        this.birthDate = updateCustomersRequest.getParsedBirthDate();
        this.phone = updateCustomersRequest.getPhone();
        this.postalCode = updateCustomersRequest.getPostalCode();
        this.primaryAddress = updateCustomersRequest.getPrimaryAddress();
        this.detailAddress = updateCustomersRequest.getDetailAddress();
        this.memo = updateCustomersRequest.getMemo();
        this.guardianName = updateCustomersRequest.getGuardianName();
        this.guardianRelationship = updateCustomersRequest.getGuardianRelationship();
        this.guardianPhone = updateCustomersRequest.getGuardianPhone();
        this.status = updateCustomersRequest.getCustomerStatus();
        this.employeeCode = employeeCode;
    }
}
