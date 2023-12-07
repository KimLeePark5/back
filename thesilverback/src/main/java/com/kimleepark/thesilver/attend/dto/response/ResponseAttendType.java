package com.kimleepark.thesilver.attend.dto.response;

import com.kimleepark.thesilver.employee.Employee;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
@Slf4j
@ToString
public class ResponseAttendType {
    private final long empNo;
    //결근
    private final int absentCount;
    //지각
    private final int lateCount;
    //휴가
    private final int vacationCount;
    //조퇴
    private final int leaveEarlyCount;
    //이번달 근무시간
    private final int totalAttendTime;

    public static ResponseAttendType getAttendTypeCount(List<ResponseAttend> responseAttend, Long empNo){
        // 지각횟수
        int lateCount = 0;

        // 휴가횟수
        int vacationCount = 0;

        //결근횟수
        int absentCount = 0;

        //조퇴횟수
        int leaveEarlyCount = 0;

        //근무시간
        int attendTime = 0;

        for(ResponseAttend attend : responseAttend){
            attendTime += attend.getAttendTime();
            switch (attend.getNote()){
                case "지각" : lateCount += 1;
                    break;
                case "휴가" : vacationCount += 1;
                    break;
                case "결근" : absentCount += 1;
                    break;
                case "조퇴" : leaveEarlyCount += 1;
                    break;
            }
        }
        return new ResponseAttendType( empNo,absentCount,lateCount,vacationCount,leaveEarlyCount,attendTime);
    }



    public static ResponseAttendType getAttendTypeCountAdmin(Employee employee,LocalDate start, LocalDate end) {

        List<ResponseAttend> list = employee.getAttendList().stream().filter(att -> att.getAttendDate().isAfter(start)&&att.getAttendDate().isBefore(end)).map(attend ->ResponseAttend.from(attend,employee.getEmployeeCode())).collect(Collectors.toList());


        return getAttendTypeCount(list,employee.getEmployeeCode());
    }
}
