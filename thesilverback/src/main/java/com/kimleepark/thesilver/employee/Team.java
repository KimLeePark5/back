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
@Table(name="tbl_team")
public class Team {

    @Id
    private Long teamCode;

    @Column
    private String teamName;
}
