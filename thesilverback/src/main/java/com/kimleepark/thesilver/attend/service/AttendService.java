package com.kimleepark.thesilver.attend.service;

import com.kimleepark.thesilver.attend.domain.Attend;
import com.kimleepark.thesilver.attend.domain.ModifiedAttend;
import com.kimleepark.thesilver.attend.domain.repository.AttendRepository;
import com.kimleepark.thesilver.attend.domain.repository.ModifiedAttendRepository;
import com.kimleepark.thesilver.attend.dto.ResponseModifiedAttend;
import com.kimleepark.thesilver.attend.dto.request.RequestAttend;
import com.kimleepark.thesilver.attend.dto.response.ResponseAttend;
import com.kimleepark.thesilver.attend.dto.response.ResponseAttendAdmin;
import com.kimleepark.thesilver.attend.dto.response.ResponseAttendAdminAndModifiedAttend;
import com.kimleepark.thesilver.attend.dto.response.ResponseAttendType;
import com.kimleepark.thesilver.common.exception.BadRequestException;
import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.kimleepark.thesilver.common.exception.type.ExceptionCode.ALREADY_EXIST_ATTEND;
import static com.kimleepark.thesilver.common.exception.type.ExceptionCode.NOT_FOUND_MEMBER_CODE;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AttendService {
    private final static String EARLY_LEAVE = "조퇴";
    private final static String LATE = "지각";

    private final AttendRepository attendRepository;
    private final ModifiedAttendRepository modifiedAttendRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public List<ResponseAttend> getEmpAttend(int empNo, String month) {
        log.info("month : {}", month);
        String date = month + "-01";
        LocalDate start = LocalDate.parse(date);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        Employee employee = employeeRepository.findById((long)empNo).orElseThrow(() -> new IllegalArgumentException());
        List<Attend> attendList = attendRepository.findByEmployeeCodeAndAttendDateBetween(employee, start, end);
        log.info("attendList : {}", attendList);

        return attendList.stream().map(attend -> ResponseAttend.from(attend,(long)empNo)).collect(Collectors.toList());
    }

    public void enterTimeSave(int empNo) {
        Employee employee = employeeRepository.findById((long) empNo).orElseThrow(() -> new IllegalArgumentException());
        Attend newAttend = Attend.of();
        Attend.setEmp(newAttend, employee);

        LocalTime limit = LocalTime.of(9, 10);
        LocalTime now = LocalTime.now();

        if (now.isAfter(limit)) {
            newAttend.updateNote(LATE);
        }
        attendRepository.save(newAttend);
        log.info("newAttend : {} ", newAttend);
    }

    public void leaveTimeSave(int empNo, LocalDate today) {
        Employee employee = employeeRepository.findById((long) empNo).orElseThrow(() -> new IllegalArgumentException());

        Attend attend = attendRepository.findByEmployeeCodeAndAttendDate(employee, today)
                .orElseThrow(() -> new IllegalArgumentException());

        log.info("attend : {}", attend);

        LocalTime now = LocalTime.now();
        LocalTime limit = LocalTime.of(18, 30);
        LocalTime leaveEarly = LocalTime.of(17, 50);


        if (now.isAfter(limit)) {
            attend.updateType();
        }
        if (now.isBefore(leaveEarly)) {
            attend.updateNote(EARLY_LEAVE);
        }
        attend.setLeaveTime();


        Duration diff = Duration.between(attend.getEntertime(), attend.getLeavetime());
        int attendTime =  diff.toHoursPart();
        attend.setAttendTime(attendTime);
    }


    public ResponseAttend getTodayAttend(int empNo, LocalDate today) {
        Employee employee = employeeRepository.findById((long) empNo).orElseThrow(() -> new IllegalArgumentException());
        Attend attend = attendRepository.findByEmployeeCodeAndAttendDate(employee, today)
                .orElseThrow(() -> new IllegalArgumentException());

        return ResponseAttend.from(attend,(long)empNo);
    }

    public void modifyAttend(int empNo, int attendNo, RequestAttend requestAttend) {
        Employee employee = employeeRepository.findById((long) empNo).orElseThrow(() -> new IllegalArgumentException());
        Attend attend = attendRepository.findById(attendNo)
                .orElseThrow(() -> new IllegalArgumentException());
        ModifiedAttend newModifiedAttend = ModifiedAttend.of(attend, requestAttend, employee);
        attend.updateAttend(requestAttend);
        modifiedAttendRepository.save(newModifiedAttend);
    }


    public Page<ResponseModifiedAttend> getModifiedAttend(int currentPage, int modifiedNo) {


        Page<ModifiedAttend> attendHistory = modifiedAttendRepository.findByAttendNo(getPageable(currentPage), modifiedNo);

        log.info("attendHistory : {}", attendHistory.getContent());

        return attendHistory.map(history -> ResponseModifiedAttend.from(history));

    }

    private Pageable getPageable(int currentPage) {
        return PageRequest.of(currentPage - 1, 10);
    }


    @Transactional(readOnly = true)
    public void dupcheckToday(int empNo) {
        LocalDate today = LocalDate.now();
        Employee employee = employeeRepository.findById((long) empNo).orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_CODE));
        if (attendRepository.existsByEmployeeCodeAndAttendDate(employee, today)) {
            throw new BadRequestException(ALREADY_EXIST_ATTEND);
        }

    }

    public ResponseAttendAdminAndModifiedAttend getAttendAdmin(final Integer page,final String month) {

        String date = month + "-01";
        LocalDate start = LocalDate.parse(date).minusDays(1);
        LocalDate end = LocalDate.parse(date).plusMonths(1);

        List<ModifiedAttend> modifiedAttends = modifiedAttendRepository.findAll();

        Page<Employee> employees = employeeRepository.findAll(getPageable(page));

        Page<ResponseAttendAdmin> responseAttendAdminList = employees.map(employee -> ResponseAttendAdmin.from(employee,start,end));
        List<ResponseModifiedAttend> responseModifiedAttends = modifiedAttends.stream().map(modifylist -> ResponseModifiedAttend.from(modifylist)).collect(Collectors.toList());

        Page<ResponseAttendType> responseAttendTypes = employees.map(employee -> ResponseAttendType.getAttendTypeCountAdmin(employee,start,end));


        ResponseAttendAdminAndModifiedAttend lists = ResponseAttendAdminAndModifiedAttend.of(responseAttendAdminList, responseModifiedAttends,responseAttendTypes);
        return lists;

    }

    public ResponseAttendAdminAndModifiedAttend getAttendAdminByName(Integer page, String month, String name) {
        String date = month + "-01";
        LocalDate start = LocalDate.parse(date);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<ModifiedAttend> modifiedAttends = modifiedAttendRepository.findAll();

        Page<Employee> employees = employeeRepository.findByEmployeeNameContaining(getPageable(page),name);

        Page<ResponseAttendAdmin> responseAttendAdminList = employees.map(employee -> ResponseAttendAdmin.from(employee,start,end));
        List<ResponseModifiedAttend> responseModifiedAttends = modifiedAttends.stream().map(modifylist -> ResponseModifiedAttend.from(modifylist)).collect(Collectors.toList());

        Page<ResponseAttendType> responseAttendTypes = employees.map(employee -> ResponseAttendType.getAttendTypeCountAdmin(employee,start,end));


        ResponseAttendAdminAndModifiedAttend lists = ResponseAttendAdminAndModifiedAttend.of(responseAttendAdminList, responseModifiedAttends,responseAttendTypes);
        return lists;
    }
}
