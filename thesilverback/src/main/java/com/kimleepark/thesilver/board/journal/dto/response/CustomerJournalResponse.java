package com.kimleepark.thesilver.board.journal.dto.response;

import com.kimleepark.thesilver.attachment.Attachment;
import com.kimleepark.thesilver.board.journal.domain.Journal;
import com.kimleepark.thesilver.board.program.domain.Program;
import com.kimleepark.thesilver.board.program.dto.response.CustomerProgramResponse;
import com.kimleepark.thesilver.customer.domain.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerJournalResponse {

    private String categoryName;
    private String round;               // 회차
    private LocalDate observation;      //참관 일자
    private String programTopic;        // 프로그램 주제
    private LocalTime startTime;        // 수업 시작 시간
    private LocalTime endTime;           // 수업 종료 시간
    private String day;                 // 요일
    //    private Long employeeCode;        // 직원 코드
    private String employeeName;        // 직원 이름
    private String teacherName;         // 강사 이름
    private String subProgress;          //프로그램 진행 사항
    private String observe;             // 관찰 결과
    private String rating;              //평가
    private String note;                //비고
    private String participantNames; // 참가자들의 이름을 쉼표로 구분하여 이어서 저장하는 문자열
    private List<String> attachmentUrls;  // 첨부파일 URL 리스트


    public static CustomerJournalResponse from(Journal journal) {

        // 해당 일지에 있는 모든 참가자의 이름을 리스트로 가져옴
        List<String> participantNamesList = journal.getParticipants().stream()
                .map(participant -> participant.getCustomer().getName())
                .collect(Collectors.toList());

        // 여러 참가자가 있는 경우 이름을 쉼표와 공백으로 구분하여 조인
        String participantNames = String.join(", ", participantNamesList);

        // 해당 일지에 있는 모든 첨부파일의 URL을 리스트로 가져옴
        List<String> attachmentUrls = journal.getAttachments().stream()
                .map(Attachment::getUrl)
                .collect(Collectors.toList());

        return new CustomerJournalResponse(
                journal.getProgram().getCategory().getCategoryName(),
                journal.getProgram().getRound(),
                journal.getObservation(),
                journal.getProgramTopic(),
                journal.getProgram().getStartTime(),
                journal.getProgram().getEndTime(),
                journal.getProgram().getDay(),
//                journal.getProgram().getEmployeeCode(),
                journal.getEmployee().getEmployeeName(),
                journal.getProgram().getTeacher().getTeacherName(),
                journal.getSubProgress(),
                journal.getObserve(),
                journal.getRating(),
                journal.getNote(),
                participantNames,
                attachmentUrls
        );
    }

}
