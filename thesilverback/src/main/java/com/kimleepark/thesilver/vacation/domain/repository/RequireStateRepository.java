package com.kimleepark.thesilver.vacation.domain.repository;

import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.jwt.CustomUser;
import com.kimleepark.thesilver.vacation.domain.Require;
import com.kimleepark.thesilver.vacation.domain.type.RequireStatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RequireStateRepository extends JpaRepository<Require, CustomUser> {

    /* 직원 코드로 연차 상신 현황 조회 */
    List<Require> findByEmployeeEmployeeCode(Long employeeCode);

    boolean existsByEmployeeAndEndDate(Employee employee, LocalDateTime now);

    boolean existsByEmployeeAndEndDateAndVacationTypeVacationNameAndReqStatus(Employee employee, LocalDateTime foramtDate, String vac, RequireStatusType pass);

    boolean existsByEmployeeAndEndDateAndVacationTypeVacationName(Employee employee, LocalDateTime foramtDate, String name);
}
