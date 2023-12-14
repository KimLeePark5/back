package com.kimleepark.thesilver.board.journal.domain;

import com.kimleepark.thesilver.attachment.Attachment;
import com.kimleepark.thesilver.board.participant.Participant;
import com.kimleepark.thesilver.board.program.domain.Program;
import com.kimleepark.thesilver.board.program.domain.ProgramCategory;
import com.kimleepark.thesilver.board.program.domain.Teacher;
import com.kimleepark.thesilver.employee.Employee;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_journal")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long journalCode; // 일지 번호
    @Column(nullable = false)
    private String subProgress; //프로그램 진행 사항
    @Column(nullable = false)
    private String observe; // 관찰 결과
    @Column(nullable = false)
    private String rating; //평가
    @Column(nullable = false)
    private String note; //비고
    @Column(nullable = false)
    private LocalDate observation; //참관 일자
    @Column(nullable = false)
    private String programTopic; // 프로그램 주제

    @ManyToOne(fetch = FetchType.LAZY)          //프로그램
    @JoinColumn(name = "code")
    private Program program;

    @ManyToOne(fetch = FetchType.LAZY)          //직원
    @JoinColumn(name = "employeeCode")
    private Employee employee;

    // mappedBy 속성은 양방향 관계에서 사용되어 어떤 필드가 연관 관계의 주인(Owner)인지를 나타냄
    //@OneToMany(mappedBy = "journal", fetch = FetchType.LAZY)
    //@JoinColumn(name = "participantCode") //@JoinColumn는 외래키 지정하는데 mappedBy랑 같이 쓰면 안됨
    //private List<Participant> participants;        //참석자들

    @OneToMany(mappedBy = "journal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>(); // 참석자들


    @OneToMany(mappedBy = "journal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments = new ArrayList<>();       // 첨부파일들

    public Journal(Long journalCode, String subProgress, String observe, String rating, String note,
                   LocalDate observation, String programTopic, Program program, Employee employee, List<Participant> participants, List<Attachment> attachments
    ) {
        this.journalCode = journalCode;
        this.subProgress = subProgress;
        this.observe = observe;
        this.rating = rating;
        this.note = note;
        this.observation = observation;
        this.programTopic = programTopic;
        this.program = program;
        this.employee = employee;
        this.participants = participants;
        this.attachments = attachments;
    }

    // 참석자들 설정
    public void setParticipants(List<Participant> participants) {
        this.participants.clear();
        if (participants != null) {
            this.participants.addAll(participants);
            participants.forEach(participant -> participant.setJournal(this));
        }
    }

    public void setJournalCode(Long journalCode) {
        this.journalCode = journalCode;
    }

    // setProgram 메서드 추가
    public void setProgram(Program program) {
        // program이 null이면 새 Program 인스턴스를 생성
        if (program == null) {
            this.program = new Program();
        } else {
            // program이 null이 아니면 주어진 program으로 설정
            this.program = program;
        }
    }
}