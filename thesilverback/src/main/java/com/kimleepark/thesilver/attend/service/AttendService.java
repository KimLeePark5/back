package com.kimleepark.thesilver.attend.service;

import com.kimleepark.thesilver.attend.domain.Attend;
import com.kimleepark.thesilver.attend.domain.ModifiedAttend;
import com.kimleepark.thesilver.attend.domain.repository.AttendRepository;
import com.kimleepark.thesilver.attend.domain.repository.ModifiedAttendRepository;
import com.kimleepark.thesilver.attend.domain.type.AttendType;
import com.kimleepark.thesilver.attend.dto.ResponseModifiedAttend;
import com.kimleepark.thesilver.attend.dto.request.RequestAttend;
import com.kimleepark.thesilver.attend.dto.response.*;
import com.kimleepark.thesilver.common.exception.BadRequestException;
import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.employee.repository.EmployeeRepository;
import com.kimleepark.thesilver.vacation.domain.Require;
import com.kimleepark.thesilver.vacation.domain.VacationType;
import com.kimleepark.thesilver.vacation.domain.repository.RequireRepository;
import com.kimleepark.thesilver.vacation.domain.repository.RequireStateRepository;
import com.kimleepark.thesilver.vacation.domain.repository.VacationTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.kimleepark.thesilver.common.exception.type.ExceptionCode.ALREADY_EXIST_ATTEND;
import static com.kimleepark.thesilver.common.exception.type.ExceptionCode.NOT_FOUND_MEMBER_CODE;
import static com.kimleepark.thesilver.vacation.domain.type.RequireStatusType.PASS;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
@EnableScheduling
public class AttendService {

    private final RequireStateRepository requireStateRepository;
    private final AttendRepository attendRepository;
    private final ModifiedAttendRepository modifiedAttendRepository;
    private final EmployeeRepository employeeRepository;
    private final VacationTypeRepository vacationTypeRepository;
    private final RequireRepository requireRepository;

    @Transactional(readOnly = true)
    public List<ResponseAttend> getEmpAttend(long empNo, String month) {
        log.info("month : {}", month);
        String date = month + "-01";
        LocalDate start = LocalDate.parse(date);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        Employee employee = employeeRepository.findById(empNo).orElseThrow(() -> new IllegalArgumentException());
        List<Attend> attendList = attendRepository.findByEmployeeCodeAndAttendDateBetween(employee, start, end);
        log.info("attendList : {}", attendList);

        return attendList.stream().map(attend -> ResponseAttend.from(attend,empNo)).collect(Collectors.toList());
    }

    public void enterTimeSave(long empNo) {
        Employee employee = employeeRepository.findById(empNo).orElseThrow(() -> new IllegalArgumentException());
        Attend attend = attendRepository.findByEmployeeCodeAndAttendDate(employee,LocalDate.now()).orElseThrow(()->new IllegalArgumentException());
        attend.putEnterTime();

        LocalTime limit = LocalTime.of(9, 10);
        LocalTime now = LocalTime.now();
        LocalDateTime nowDate = LocalDateTime.now();
        log.info("12312312321 :{}",requireStateRepository.existsByEmployeeAndEndDateAndVacationTypeVacationNameAndReqStatus(employee,LocalDateTime.of(nowDate.getYear(),nowDate.getMonth(),nowDate.getDayOfMonth(),13,0),"오전반차",PASS));
        if (now.isAfter(limit)) {
            if(requireStateRepository.existsByEmployeeAndEndDateAndVacationTypeVacationNameAndReqStatus(employee,LocalDateTime.of(nowDate.getYear(),nowDate.getMonth(),nowDate.getDayOfMonth(),13,0),"오전반차",PASS)){
                attend.updateMorningOff();
            }else{
                attend.updateLate();
            }

        }

    }

    public void leaveTimeSave(long empNo, LocalDate today) {
        Employee employee = employeeRepository.findById(empNo).orElseThrow(() -> new IllegalArgumentException());

        Attend attend = attendRepository.findByEmployeeCodeAndAttendDate(employee, today)
                .orElseThrow(() -> new IllegalArgumentException());

        log.info("attend : {}", attend);

        LocalTime now = LocalTime.now();

        LocalTime limit = LocalTime.of(18, 30);

        LocalTime leaveEarly = LocalTime.of(20, 50);
        LocalDateTime today2 = LocalDateTime.now();

        LocalDateTime foramtDate = LocalDateTime.of(today2.getYear(),today2.getMonth(),today2.getDayOfMonth(),0,0);
        attend.updateTypeDefault();
        if (now.isAfter(limit)) {
            attend.updateType();
        }

        attend.setLeaveTime();
        Duration diff = Duration.between(attend.getEntertime(), attend.getLeavetime());
        int attendTime =  diff.toHoursPart();

        if(attendTime >= 4 ){
            attend.setAttendTime(attendTime - 1);
        }else{
            attend.setAttendTime(attendTime);
        }



        if (now.isBefore(leaveEarly)) {
            if(requireStateRepository.existsByEmployeeAndEndDateAndVacationTypeVacationNameAndReqStatus(employee,foramtDate,"오후반차",PASS)){
                attend.updateafternoonoff();
            }else{
                attend.updateLeaveEarly();
            }
        }
    }


    public ResponseAttend getTodayAttend(long empNo, LocalDate today) {
        Employee employee = employeeRepository.findById(empNo).orElseThrow(() -> new IllegalArgumentException());
        Attend attend = attendRepository.findByEmployeeCodeAndAttendDate(employee, today)
                .orElseThrow(() -> new IllegalArgumentException());

        return ResponseAttend.from(attend,empNo);
    }

    public void modifyAttend(long empNo, int attendNo, RequestAttend requestAttend) {
        Employee employee = employeeRepository.findById(empNo).orElseThrow(() -> new IllegalArgumentException());
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




    @Transactional(readOnly = true)
    public void dupcheckToday(long empNo) {
        LocalDate today = LocalDate.now();
        Employee employee = employeeRepository.findById(empNo).orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_CODE));
        Attend attend = attendRepository.findByEmployeeCodeAndAttendDate(employee, today).orElseThrow(()->new IllegalArgumentException());
        if(attend.getEntertime() != null){
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
        LocalDate start = LocalDate.parse(date).minusDays(1);
        LocalDate end = LocalDate.parse(date).plusMonths(1);


        List<ModifiedAttend> modifiedAttends = modifiedAttendRepository.findAll();

        Page<Employee> employees = employeeRepository.findByEmployeeNameContaining(getPageable(page),name);

        Page<ResponseAttendAdmin> responseAttendAdminList = employees.map(employee -> ResponseAttendAdmin.from(employee,start,end));
        List<ResponseModifiedAttend> responseModifiedAttends = modifiedAttends.stream().map(modifylist -> ResponseModifiedAttend.from(modifylist)).collect(Collectors.toList());

        Page<ResponseAttendType> responseAttendTypes = employees.map(employee -> ResponseAttendType.getAttendTypeCountAdmin(employee,start,end));

        ResponseAttendAdminAndModifiedAttend lists = ResponseAttendAdminAndModifiedAttend.of(responseAttendAdminList, responseModifiedAttends,responseAttendTypes);
        return lists;
    }

    public ResponseAttendAdminAndModifiedAttend getAttendByCategory(Integer page, String value,String month) {
        log.info("aaaapage:{}",page);
        log.info("vaa:{}",value);
        String date = month + "-01";
        LocalDate start = LocalDate.parse(date).minusDays(1);
        LocalDate end = LocalDate.parse(date).plusMonths(1);

        List<ModifiedAttend> modifiedAttends = modifiedAttendRepository.findAll();
        List<ResponseModifiedAttend> responseModifiedAttends = modifiedAttends.stream().map(modifylist -> ResponseModifiedAttend.from(modifylist)).collect(Collectors.toList());

        Page<Employee> employees = employees = employeeRepository.findAllByOrderByTeam(getPageable(page));
        Page<ResponseAttendAdmin> pageResponseAttendAdmin = employees.map(emp->ResponseAttendAdmin.from(emp,start,end));
        Page<ResponseAttendType> responseAttendTypes = responseAttendTypes = employees.map(employee -> ResponseAttendType.getAttendTypeCountAdmin(employee,start,end));
        ResponseAttendAdminAndModifiedAttend lists = lists = ResponseAttendAdminAndModifiedAttend.of(pageResponseAttendAdmin, responseModifiedAttends,responseAttendTypes);


        return lists;
    }

    private Pageable getPageable(int currentPage) {
        return PageRequest.of(currentPage - 1, 10);
    }
    public ResponseAttendAndModify getAttendByLate(Integer page, String value, String month) {
        String date = month + "-01";

        LocalDate start = LocalDate.parse(date).minusDays(1);
        LocalDate end = LocalDate.parse(date).plusMonths(1);
        List<ModifiedAttend> modifiedAttends = modifiedAttendRepository.findAll();
        List<ResponseModifiedAttend> responseModifiedAttends = modifiedAttends.stream().map(modifylist -> ResponseModifiedAttend.from(modifylist)).collect(Collectors.toList());


        List<Employee> employees = employeeRepository.findAll();
        List<ResponseAttendAdminTwo> two = employees.stream().map(emp->ResponseAttendAdminTwo.of(emp,start,end)).collect(Collectors.toList());

        switch (value){
            case "abs" : two.sort(Comparator.comparing(ResponseAttendAdminTwo::getAbsentCount).reversed());
            break;
            case "late" : two.sort(Comparator.comparing(ResponseAttendAdminTwo::getLateCount).reversed());
                break;
            case "leaveE" : two.sort(Comparator.comparing(ResponseAttendAdminTwo::getLeaveEarlyCount).reversed());
                break;
            case "vac" : two.sort(Comparator.comparing(ResponseAttendAdminTwo::getVacCount).reversed());
                break;
            case "atTime" : two.sort(Comparator.comparing(ResponseAttendAdminTwo::getAttendTime).reversed());
                break;
            case "team" : two.sort(Comparator.comparing(ResponseAttendAdminTwo::getTeam));
            break;
        }

        PageRequest pageRequest = PageRequest.of(page-1,10);
        int startList = (int) pageRequest.getOffset();
        int endList = Math.min((startList + pageRequest.getPageSize()), two.size());
        Page<ResponseAttendAdminTwo> pageResponseAttendAdmin = new PageImpl<>(two.subList(startList,endList),pageRequest, two.size());


        ResponseAttendAndModify responseAttendAndModify = ResponseAttendAndModify.of(responseModifiedAttends,pageResponseAttendAdmin);
        return responseAttendAndModify;
    }


//    @Scheduled(cron = "0 0 05 * * ?")
//    public void scheduleAttned(){
//
//        List<Employee> employees  = employeeRepository.findAll();
//        List<Attend> attendList = employees.stream().map(emp -> Attend.getEmpNo(emp)).collect(Collectors.toList());
//
//        attendRepository.saveAll(attendList);
//    }
//
//    @Scheduled(cron = "0 00 15 * * MON-FRI")
//    public void absentSchedule(){
//        List<Attend> attendList = attendRepository.findByAttendDateAndEntertimeIsNull(LocalDate.now());
//
//        attendList.forEach(att -> att.updateAbsent());
//    }
}
