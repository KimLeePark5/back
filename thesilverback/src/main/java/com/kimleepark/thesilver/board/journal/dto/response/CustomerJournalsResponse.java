package com.kimleepark.thesilver.board.journal.dto.response;

import com.kimleepark.thesilver.board.journal.domain.Journal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerJournalsResponse {

    private Long journalCode; // 일지 번호
    private Long employeeCode;
    private Long categoryCode;
    private String employeeName; // 직원 이름
    private String categoryName; // 카테고리 프로그램명
    private String programTopic; // 프로그램 주제
    private String participantNames;
    private LocalDate observation; //참관 일자
    private String teacherName; // 강사 이름
    private int numberOfParticipants; // 참가자 수

    public static CustomerJournalsResponse from(Journal journal) {
        // 해당 일지에 있는 모든 참가자의 이름을 리스트로 가져옴
        List<String> participantNamesList = journal.getParticipants().stream()
                .map(participant -> participant.getCustomer().getName())
                .collect(Collectors.toList());
        // 참가자 수
        int numberOfParticipants = participantNamesList.size();
        // 여러 참가자가 있는 경우 이름을 쉼표와 공백으로 구분하여 조인
        String participantNames = String.join(", ", participantNamesList);

        return new CustomerJournalsResponse(
                journal.getJournalCode(),
                journal.getEmployee().getEmployeeCode(),
                journal.getProgram().getCategory().getCategoryCode(),
                journal.getEmployee().getEmployeeName(),
                journal.getProgram().getCategory().getCategoryName(),
                journal.getProgramTopic(),
                participantNames,
                journal.getObservation(),
                journal.getProgram().getTeacher().getTeacherName(),
                numberOfParticipants // 참가자 수 추가
        );
    }
}
