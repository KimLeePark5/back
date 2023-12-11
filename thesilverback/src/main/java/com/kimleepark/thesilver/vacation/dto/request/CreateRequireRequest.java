package com.kimleepark.thesilver.vacation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.employee.Rank;
import com.kimleepark.thesilver.vacation.domain.Require;
import com.kimleepark.thesilver.vacation.domain.VacationType;
import com.kimleepark.thesilver.vacation.domain.type.RequireStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class CreateRequireRequest {

    @NotNull
    private Long vacationTypeCode;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    @NotNull
    private String reqContent;
    @NotNull
    private RequireStatusType reqStatus;
    private Long approverCode;

}
