package com.kimleepark.thesilver.Program.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

@Entity
@Table(name = "tbl_program_category")
@NoArgsConstructor(access = AccessLevel.PUBLIC) //(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class ProgramCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryCode;

    @Column( nullable = false)
    private String categoryName;

    // 생성자 추가
    public ProgramCategory(String categoryName) {
        this.categoryName = categoryName;
    }
}