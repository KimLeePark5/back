package com.kimleepark.thesilver.board.program.dto.response;

import com.kimleepark.thesilver.board.program.domain.Program;
import com.kimleepark.thesilver.board.program.domain.ProgramCategory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class ResponseProgram {
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String day;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final String categoryName;

    public static ResponseProgram from(Program program, List<ProgramCategory> categoryList) {

        return new ResponseProgram(
                program.getStartDate(),
                program.getEndDate(),
                program.getDay(),
                program.getStartTime(),
                program.getEndTime(),
                categoryList.get(program.getCategory().getCategoryCode().intValue() - 1).getCategoryName()
        );
    }
}
