package com.kimleepark.thesilver.attend.service;

import com.kimleepark.thesilver.attend.domain.Attend;
import com.kimleepark.thesilver.attend.domain.ModifiedAttend;
import com.kimleepark.thesilver.attend.domain.repository.AttendRepository;
import com.kimleepark.thesilver.attend.domain.repository.ModifiedAttendRepository;
import com.kimleepark.thesilver.attend.dto.ResponseModifiedAttend;
import com.kimleepark.thesilver.attend.dto.request.RequestAttend;
import com.kimleepark.thesilver.attend.dto.response.ResponseAttend;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AttendService {
    private final static String EARLY_LEAVE = "조퇴";
    private final static String LATE = "지각";

    private final AttendRepository attendRepository;
    private final ModifiedAttendRepository modifiedAttendRepository;
    @Transactional(readOnly = true)
    public List<ResponseAttend> getEmpAttend(int empNo, String month) throws ParseException {
        log.info("month : {}",month);
        String date = month+"-01";
        LocalDate start = LocalDate.parse(date);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<Attend> attendList = attendRepository.findByEmployeeCodeAndAttendDateBetween(empNo, start,end);
        log.info("attendList : {}",attendList);
        return  attendList.stream().map(attend -> ResponseAttend.from(attend)).collect(Collectors.toList());
    }

    public void enterTimeSave(int empNo) {

        Attend newAttend = Attend.of();
        Attend.setEmp(newAttend,empNo);

        LocalTime limit = LocalTime.of(9,10);
        LocalTime now = LocalTime.now();

        if(now.isAfter(limit)){
            newAttend.updateNote(LATE);
        }
        attendRepository.save(newAttend);

        log.info("newAttend : {} ", newAttend);

    }

    public void leaveTimeSave(int empNo, LocalDate today) {
        Attend attend = attendRepository.findByEmployeeCodeAndAttendDate(empNo, today)
                        .orElseThrow(()->new IllegalArgumentException());
        log.info("attend : {}", attend);

        LocalTime now = LocalTime.now();
        LocalTime limit = LocalTime.of(18,30);
        LocalTime leaveEarly = LocalTime.of(17,50);


        if(now.isAfter(limit)){
            attend.updateType();
        }
        if(now.isBefore(leaveEarly)){
            attend.updateNote(EARLY_LEAVE);
        }

        attend.setLeaveTime();

        Duration diff = Duration.between(attend.getEntertime(), attend.getLeavetime());
        float attendTime = (float) diff.toMinutesPart()/60;
        attend.setAttendTime(attendTime);
    }


    public ResponseAttend getTodayAttend(int empNo, LocalDate today) {
        Attend attend = attendRepository.findByEmployeeCodeAndAttendDate(empNo, today)
                .orElseThrow(()->new IllegalArgumentException());

        return ResponseAttend.from(attend);
    }

    public void modifyAttend(int empNo, int attendNo, RequestAttend requestAttend) {
        Attend attend = attendRepository.findById(attendNo)
                .orElseThrow(()-> new IllegalArgumentException());
        ModifiedAttend newModifiedAttend = ModifiedAttend.of(attend,requestAttend,empNo);
        attend.updateAttend(requestAttend);
        modifiedAttendRepository.save(newModifiedAttend);
    }



    public Page<ResponseModifiedAttend> getModifiedAttend(int currentPage, int modifiedNo) {
        Page<ModifiedAttend> attendHistory = modifiedAttendRepository.findByAttendNo(getPageable(currentPage),modifiedNo);
        log.info("attendHistory : {}", attendHistory.getContent());
        return null;
    }

    private Pageable getPageable(int currentPage) {
        return PageRequest.of(currentPage-1,6);
    }


}
