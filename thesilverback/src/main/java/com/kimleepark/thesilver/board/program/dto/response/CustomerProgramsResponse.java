package com.kimleepark.thesilver.board.program.dto.response;

import com.kimleepark.thesilver.board.program.domain.Program;
import lombok.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class CustomerProgramsResponse {

    private String categoryName; // 카테고리 프로그램 이름
    private String shortStory; //프로그램 내용
    private String teacherName; //강사이름
    private Long code; // 프로그램 코드


    public static CustomerProgramsResponse from(Program program) {
        return new CustomerProgramsResponse(
                program.getCategory().getCategoryName(),
                program.getShortStory(),
                program.getTeacher().getTeacherName(),
                program.getCode()
        );
    }


}
