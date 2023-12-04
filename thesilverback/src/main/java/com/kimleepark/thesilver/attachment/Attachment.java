package com.kimleepark.thesilver.attachment;


import com.kimleepark.thesilver.board.journal.domain.Journal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tbl_attachment")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attachmentCode;       // 첨부파일 코드

    @Column(nullable = false)
    private String url;                // 사진 url

    @Column(nullable = false)
    private Long seperation;           // 구분

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journalCode")
    private Journal journal;         // 일지

    @Column(nullable = false)
    private Long reqNo;                // 상신 번호
}
