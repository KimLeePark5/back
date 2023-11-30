package com.kimleepark.thesilver.vacation.domain;


import com.kimleepark.thesilver.employee.Employee;
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


    public Vacation(Long vacationCode, LocalDateTime occurYear, Long occurVacation, Long useVacation, Employee employee) {
        this.vacationCode = vacationCode;
        this.occurYear = occurYear;
        this.occurVacation = occurVacation;
        this.useVacation = useVacation;
        this.employee = employee;
    }


}

