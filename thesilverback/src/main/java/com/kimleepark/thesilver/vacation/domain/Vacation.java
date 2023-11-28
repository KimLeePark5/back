package com.kimleepark.thesilver.vacation.domain;

import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;


@Entity
@Table(name = "tbl_vacation")
@NoArgsConstructor(access = PROTECTED)
@Getter
@EntityListeners(AutoCloseable.class)
public class Vacation {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long vacationCode;

    @Column(nullable = false)
    private LocalDateTime occurYear;

    @Column(nullable = false)
    private Long occurVacation;

    @Column(nullable = false)
    private Long useVacation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeCode")
    private Employee employee;









}
