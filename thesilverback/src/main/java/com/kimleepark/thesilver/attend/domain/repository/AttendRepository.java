package com.kimleepark.thesilver.attend.domain.repository;

import com.kimleepark.thesilver.attend.domain.Attend;
import com.kimleepark.thesilver.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendRepository extends JpaRepository<Attend,Integer> {

    List<Attend> findByEmployeeCodeAndAttendDateBetween(Employee employeeCode, LocalDate start, LocalDate end);

    Optional<Attend> findByEmployeeCodeAndAttendDate(Employee empNo, LocalDate date);

    List<Attend> findByAttendDateAndEntertimeIsNull(LocalDate date);
    boolean existsByEmployeeCodeAndAttendDate(Employee empNo, LocalDate today);

}
