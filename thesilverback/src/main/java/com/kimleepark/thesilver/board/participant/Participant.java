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
    @JoinColumn(name = "journalCode")   // 일지 코드
    private Journal journal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerCode")
    private Customer customer;

    // 생성자 추가
    public Participant(Journal journal, Customer customer) {
        this.journal = journal;
        this.customer = customer;
    }
}
