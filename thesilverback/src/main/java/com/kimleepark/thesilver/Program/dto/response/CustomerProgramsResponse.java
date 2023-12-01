package com.kimleepark.thesilver.Program.dto.response;

import com.kimleepark.thesilver.Program.domain.Program;
import lombok.*;

import javax.persistence.Entity;

import static lombok.AccessLevel.PRIVATE;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProgramsResponse {

    private String categoryName;
    private String shortStory;
    private String name; // 변경된 부분

    public static CustomerProgramsResponse from(Program program) {
        return new CustomerProgramsResponse(
                program.getCategory().getCategoryName(),
                program.getShortStory(),
                program.getTeacher().getName()
        );
    }


}
