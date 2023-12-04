package com.kimleepark.thesilver.board.journal.dto.response;

import com.kimleepark.thesilver.board.journal.domain.Journal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerJournalsResponse {

    private Long journalCode; // 일지 번호
    private String employeeName; // 직원 이름
    private String categoryName; // 카테고리 프로그램명
    private LocalDate observation; //참관 일자
    private String programTopic; // 프로그램 주제
    private String teacherName; // 강사 이름
    private Long employeeCode; // 직원 번호


    public static CustomerJournalsResponse from(Journal journal) {
        return new CustomerJournalsResponse(
                journal.getJournalCode(),
                journal.getEmployee().getEmployeeName(),
                journal.getProgram().getCategory().getCategoryName(),
                journal.getObservation(),
                journal.getProgramTopic(),
                journal.getProgram().getTeacher().getTeacherName(),
                journal.getEmployee().getEmployeeCode()
        );
    }
}
