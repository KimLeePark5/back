package com.kimleepark.thesilver.vacation.domain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_employee")
@NoArgsConstructor(access = PROTECTED)
@Getter
@EntityListeners(AccessLevel.class)
public class Employee {

    @Id
    private Long employeeCode;

    @Column(nullable = false)
    private String employeeName;

    @Column(nullable = false)
    private LocalDateTime registDate;
}
