package com.kimleepark.thesilver.vacation.domain;

import com.kimleepark.thesilver.employee.Employee;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_sign")
@NoArgsConstructor(access = PROTECTED)
@Getter
@EntityListeners(AutoCloseable.class)
public class Sign {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long SignNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reqNo")
    private Require require;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeCode")
    private Employee employee;

    @Column
    private String signStatus;

    @Column
    private String returnCause;

    @Column
    private String cancelCause;

    @Column
    private LocalDate approveDate;

    @Column
    private LocalDateTime returnDate;

    @Column
    private LocalDateTime cancelDate;

    public Sign(Long signNo, Require require, Employee employee, String signStatus, String returnCause, String cancelCause, LocalDate approveDate, LocalDateTime returnDate, LocalDateTime cancelDate) {
        SignNo = signNo;
        this.require = require;
        this.employee = employee;
        this.signStatus = signStatus;
        this.returnCause = returnCause;
        this.cancelCause = cancelCause;
        this.approveDate = approveDate;
        this.returnDate = returnDate;
        this.cancelDate = cancelDate;
    }
}


