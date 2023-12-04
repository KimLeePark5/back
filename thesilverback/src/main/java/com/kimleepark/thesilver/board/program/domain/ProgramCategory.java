package com.kimleepark.thesilver.board.program.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
}