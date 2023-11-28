package com.kimleepark.thesilver.employee;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name="tbl_rank")
public class Rank {

    @Id
    private Long rankCode;

    @Column(nullable = false)
    private String rankName;
}
