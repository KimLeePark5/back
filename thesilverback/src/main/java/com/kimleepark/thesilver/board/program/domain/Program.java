package com.kimleepark.thesilver.board.program.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "tbl_program")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@ToString
@Setter
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code; // 프로그램 번호

    @Column(nullable = false)
    private LocalDateTime startDate; // 시작일

    @Column(nullable = false)
    private LocalDateTime endDate; // 종료일

    @Column(nullable = false)
    private String day; // 요일

    @Column(nullable = false)
    private String round; // 회차

    @Column(nullable = false)
    private LocalTime startTime; // 수업 시작 시간

    @Column(nullable = false)
    private LocalTime endTime; // 수업 종료 시간

    @Column(nullable = false, length = 500)
    private String shortStory; // 프로그램 내용

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code") //fk
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryCode")
    private ProgramCategory category;

    @Column(nullable = false)
    private Long employeeCode; // 직원코드

    public Program(Long code, ProgramCategory category, LocalDateTime startDate, LocalDateTime endDate,
                   String day, String round, LocalTime startTime, LocalTime endTime, String shortStory, Teacher teacher, Long employeeCode
    ) {
        this.code = code;
        this.startDate = startDate;
        this.endDate = endDate;
        this.day = day;
        this.round = round;
        this.startTime = startTime;
        this.endTime = endTime;
        this.shortStory = shortStory;
        this.teacher = teacher;
        this.category = category;
        this.employeeCode = employeeCode;
    }

}
