package com.kimleepark.thesilver.attend.domain.repository;

import com.kimleepark.thesilver.attend.domain.Attend;
import net.bytebuddy.asm.Advice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface AttendRepository extends JpaRepository<Attend,Integer> {

    List<Attend> findByEmployeeCode(int empNo);


//    @Query(value = "SELECT a FROM Attend a WHERE a.employeeCode = 1 AND a.attendDate BETWEEN '2023-11-21' AND '2023-11-31'")
    List<Attend> findByEmployeeCodeAndAttendDateBetween(int employeeCode, LocalDate start, LocalDate end);

//    @Query(value = "SELECT a FROM Attend a WHERE a.employeeCode = ?1 AND a.attendDate = ?2 ")
    Attend findByEmployeeCodeAndAttendDate(int empNo, LocalDate date);
}
