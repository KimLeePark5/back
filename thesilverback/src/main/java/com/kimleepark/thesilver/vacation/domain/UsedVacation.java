package com.kimleepark.thesilver.vacation.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.vacation.domain.type.RequireStatusType;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_require")
@NoArgsConstructor
@Getter
@EntityListeners(AutoCloseable.class)
public class UsedVacation {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long reqNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacationTypeCode")
    private VacationType vacationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeCode")
    private Employee employee;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startDate;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime endDate;

    @Column(nullable = false)
    private String reqContent;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RequireStatusType reqStatus;

    @Column(nullable = false)
    private LocalDateTime reqDate;
}
