package com.kimleepark.thesilver.attend.domain.repository;

import com.kimleepark.thesilver.attend.domain.Attend;
import net.bytebuddy.asm.Advice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AttendRepository extends JpaRepository<Attend,Integer> {

    List<Attend> findByEmployeeCodeAndAttendDateBetween(int employeeCode, LocalDate start, LocalDate end);

    Optional<Attend> findByEmployeeCodeAndAttendDate(int empNo, LocalDate date);
}
