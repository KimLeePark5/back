package com.kimleepark.thesilver.attend.service;

import com.kimleepark.thesilver.attend.domain.Attend;
import com.kimleepark.thesilver.attend.domain.repository.AttendRepository;
import com.kimleepark.thesilver.attend.dto.response.ResponseAttend;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AttendService {
    private final AttendRepository attendRepository;

    @Transactional(readOnly = true)
    public List<ResponseAttend> getEmpAttend(int empNo, int month, int year) throws ParseException {
        log.info("service start");
        LocalDate start = LocalDate.of(year,month,1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        log.info("start : {}",start);
        log.info("end : {}",end);

        List<Attend> attendList = attendRepository.findByEmployeeCodeAndAttendDateBetween(empNo, start,end);
        log.info("attendList : {}",attendList);
        return  attendList.stream().map(attend -> ResponseAttend.from(attend)).collect(Collectors.toList());
    }

    public void enterTimeSave(int empNo) {

        Attend newAttend = Attend.of();
        Attend.setEmp(newAttend,empNo);
        attendRepository.save(newAttend);

        log.info("newAttend : {} ", newAttend);

    }

    public void leaveTimeSave(int empNo, LocalDate today) {
        Attend attend = attendRepository.findByEmployeeCodeAndAttendDate(empNo, today);
        log.info("attend : {}", attend);
        Attend.setLeaveTime(attend);

    }
}
