package com.kimleepark.thesilver.board.participant;

import com.kimleepark.thesilver.board.journal.domain.Journal;
import com.kimleepark.thesilver.customer.domain.Customer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tbl_participant")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participantCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journalCode")
    private Journal journal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerCode")
    private Customer customer;

    // 생성자 추가
    public Participant(Journal journal, Customer customer) {
        this.journal = journal;
        this.customer = customer;
        // 양방향 연관관계를 설정할 때, Journal 엔터티에도 해당 참가자를 추가하는 것이 좋습니다.
        if (journal != null && !journal.getParticipants().contains(this)) {
            journal.getParticipants().add(this);
        }
    }

    public Participant(Customer customer) {
        this.customer = customer;
    }

    // Journal 설정 메서드 추가
    public void setJournal(Journal journal) {
        this.journal = journal;
        // 양방향 연관관계를 설정할 때, Journal 엔터티에도 해당 참가자를 추가하는 것이 좋습니다.
        if (journal != null && !journal.getParticipants().contains(this)) {
            journal.getParticipants().add(this);
        }
    }
}
