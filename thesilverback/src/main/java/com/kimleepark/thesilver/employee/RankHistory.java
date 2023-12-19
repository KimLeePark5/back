package com.kimleepark.thesilver.employee;

import com.kimleepark.thesilver.employee.type.RankUpDownType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.repository.EntityGraph;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name="tbl_rank_history")
public class RankHistory {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long updateRankcode;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="afterRank", insertable = false, updatable = false)
    private Rank afterRank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="beforeRank", insertable = false, updatable = false)
    private Rank beforeRank;

    @Enumerated(value = STRING)
    @Column(updatable = false)
    private RankUpDownType upDown;

    @Column
    private String updateNote;

    @Column(updatable = false)
    private Long employeeCode;
}
