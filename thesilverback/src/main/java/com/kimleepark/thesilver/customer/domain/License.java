package com.kimleepark.thesilver.customer.domain;

import com.kimleepark.thesilver.common.exception.ConflictException;
import com.kimleepark.thesilver.customer.domain.type.LicenseStatus;
import com.kimleepark.thesilver.customer.dto.request.CreateLicensesRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static com.kimleepark.thesilver.common.exception.type.ExceptionCode.START_IS_AFTER_END;
import static com.kimleepark.thesilver.customer.domain.type.LicenseStatus.ACTIVE;
import static com.kimleepark.thesilver.customer.domain.type.LicenseStatus.INACTIVE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_license")
@Getter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class License {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long licenseCode;

    @JoinColumn(name = "customerCode")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @LastModifiedDate
    @Column
    private LocalDateTime modifyDate;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private LicenseStatus status = ACTIVE;

    @Column(nullable = false)
    private Long employeeCode;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime registDate;

    public License(Customer customer, LocalDate startDate, LocalDate endDate, Long employeeCode, LicenseStatus status) {
        this.customer = customer;
        this.startDate = startDate;
        this.endDate = endDate;
        this.employeeCode = employeeCode;
        this.status = status;
    }

    public static License of(Customer customer, CreateLicensesRequest createLicensesRequest) {
        LicenseStatus status = ACTIVE;
        LocalDate today = LocalDate.now();

        if (createLicensesRequest.getStartDate().isAfter(createLicensesRequest.getEndDate())) {
            throw new ConflictException(START_IS_AFTER_END);
        } else if (createLicensesRequest.getStartDate().isAfter(today)) {
            status = INACTIVE;
        } else if (createLicensesRequest.getEndDate().isBefore(today)) {
            status = INACTIVE;
        }

        return new License(
                customer,
                createLicensesRequest.getStartDate(),
                createLicensesRequest.getEndDate(),
                1L,
                status);
    }
}
