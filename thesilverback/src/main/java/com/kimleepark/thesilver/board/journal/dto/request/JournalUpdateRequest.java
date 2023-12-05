package com.kimleepark.thesilver.board.journal.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
@Setter
public class JournalUpdateRequest {

    @Min(value = 1)
    private Long journalCode;           // 일지 번호
    @Min(value = 1)
    private  Long categoryCode;
    @NotNull
    private String categoryName;        // 카테고리 프로그램 이름
    @NotNull
    private String round;               // 회차
    @NotNull
    private LocalDate observation;      //참관 일자
    @NotNull
    private String programTopic;        // 프로그램 주제
    @NotNull
    private LocalTime startTime;        // 수업 시작 시간
    @NotNull
    private LocalTime endTime;          // 수업 종료 시간
    @NotNull
    private String day;                 // 요일

//    @Min(value = 1)
//    private Long employeeCode;          // 직원코드
    @NotNull
    private String employeeName;        // 직원 이름
    @NotNull
    private String teacherName;         // 강사 이름
    @NotNull
    private String subProgress;         //프로그램 진행 사항
    @NotNull
    private String observe;             // 관찰 결과
    @NotNull
    private String rating;              //평가
    @NotNull
    private String note;                //비고
    @NotNull
    private String participantNames;    // 참가자들의 이름을 쉼표로 구분하여 이어서 저장하는 문자열

}
