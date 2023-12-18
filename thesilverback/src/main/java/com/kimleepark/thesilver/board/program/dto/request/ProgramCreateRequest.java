package com.kimleepark.thesilver.board.program.dto.request;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;



@Getter
@RequiredArgsConstructor
public class ProgramCreateRequest {

    @Min(value = 1)
    private  Long code;

    @Min(value = 1)
    private  Long categoryCode;
    @NotNull
    private  String categoryName;

    @NotNull
    private   LocalDateTime startDate;
    @NotNull
    private   LocalDateTime endDate;
    @NotBlank
    private   String day;
    @NotBlank
    private   String round;
    @NotNull
    private  LocalTime startTime;
    @NotNull
    private  LocalTime endTime;
    @NotBlank
    private   String shortStory;
    @Min(value = 1)
    private Long employeeCode;


    @NotBlank
    private String teacherName; // 강사 이름
    @NotBlank
    private  String birthDate; // 생년월일
    @NotBlank
    private  String gender; // 강사 성별
    @NotBlank
    private  String phone; // 강사 연락처
    @NotBlank
    private  String postNo; // 우편번호
    @NotBlank
    private  String address; // 주소
    @NotBlank
    private  String detailAddress; // 강사 상세 주소

    private String profilePicture; // 강사 프로필 사진




}
