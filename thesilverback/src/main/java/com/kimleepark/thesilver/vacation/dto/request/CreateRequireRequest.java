package com.kimleepark.thesilver.vacation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kimleepark.thesilver.vacation.domain.type.RequireStatusType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreateRequireRequest {

    @NotNull
    private Long vacationTypeCode;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    @NotNull
    private String reqContent;
    @NotNull
    private RequireStatusType reqStatus;
    private Long approverCode;

}
