package com.kimleepark.thesilver.board.program.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kimleepark.thesilver.board.program.domain.Program;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProgramResponse {


    private String categoryName;

    private Long code;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private String day;
    private String round;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    private String shortStory;

    private String teacherName;
    private String birthDate;
    private String gender;
    private String phone;
    private String postNo;
    private String address;
    private String detailAddress;
    private String profilePicture;

    public static CustomerProgramResponse from(Program program){
        return new CustomerProgramResponse(
                program.getCategory().getCategoryName(),
                program.getCode(),
                program.getStartDate(),
                program.getEndDate(),
                program.getDay(),
                program.getRound(),
                program.getStartTime(),
                program.getEndTime(),
                program.getShortStory(),
                program.getTeacher().getTeacherName(),
                program.getTeacher().getBirthDate(),
                program.getTeacher().getGender(),
                program.getTeacher().getPhone(),
                program.getTeacher().getPostNo(),
                program.getTeacher().getAddress(),
                program.getTeacher().getDetailAddress(),
                program.getTeacher().getProfilePicture()
        );
    }
}