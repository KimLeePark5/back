package com.kimleepark.thesilver.attend.dto.response;

import com.kimleepark.thesilver.attend.domain.Attend;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Slf4j
@ToString
public class ResponseAttendType {
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

    public static ResponseAttendType getAttendTypeCount(List<ResponseAttend> responseAttend){
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

        return new ResponseAttendType(absentCount,lateCount,vacationCount,leaveEarlyCount,attendTime);
    }
}
