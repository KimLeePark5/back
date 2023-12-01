package com.kimleepark.thesilver.Program.dto.response;

import com.kimleepark.thesilver.Program.domain.Program;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProgramResponse {

    private Long categoryCode;
    private String categoryName;

    private Long code;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String day;
    private String round;
    private LocalTime startTime;
    private LocalTime endTime;
    private String shortStory;

    private String name;
    private String birthDate;
    private String gender;
    private String phone;
    private String postNo;
    private String address;
    private String detailAddress;
    private String profilePicture;

    public static CustomerProgramResponse from(Program program){
        return new CustomerProgramResponse(
                program.getCategory().getCategoryCode(),
                program.getCategory().getCategoryName(),
                program.getCode(),
                program.getStartDate(),
                program.getEndDate(),
                program.getDay(),
                program.getRound(),
                program.getStartTime(),
                program.getEndTime(),
                program.getShortStory(),
                program.getTeacher().getName(),
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
