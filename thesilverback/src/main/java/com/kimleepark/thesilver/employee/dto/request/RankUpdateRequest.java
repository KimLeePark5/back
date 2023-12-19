package com.kimleepark.thesilver.employee.dto.request;

import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.employee.type.RankUpDownType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class RankUpdateRequest {
    @NotNull
    private final Long beforeRank;
    @NotNull
    private final Long afterRank;
    @NotNull
    private final RankUpDownType upDown;
    @NotNull
    private final Employee employeeCode;

    private final String updateNote;
}
