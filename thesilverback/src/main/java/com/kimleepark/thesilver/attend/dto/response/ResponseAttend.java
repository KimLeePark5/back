package com.kimleepark.thesilver.attend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kimleepark.thesilver.attend.domain.Attend;
import com.kimleepark.thesilver.employee.Employee;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Getter
@RequiredArgsConstructor
@ToString
public class ResponseAttend {
    private final Long empNo;
    private final int attendCode;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate attendDate;
    private final LocalTime enterTime;
    private final LocalTime leaveTime;
    private final float attendTime;
    private final String type;
    private final String note;


    public static ResponseAttend from(Attend attend,Long empNo){
        return new ResponseAttend(
                attend.getEmployeeCode().getEmployeeCode(),
                attend.getAttendNo(),
                attend.getAttendDate(),
                attend.getEntertime(),
                attend.getLeavetime(),
                attend.getAttendTime(),
                attend.getType(),
                attend.getNote()
        );
    }

}
