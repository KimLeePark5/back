package com.kimleepark.thesilver.vacation.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "tbl_vacation_type")
@NoArgsConstructor
@Getter
@EntityListeners(AutoCloseable.class)
public class VacationType {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long vacationTypeCode;

    @Column(nullable = false)
    private String vacationName;

}
