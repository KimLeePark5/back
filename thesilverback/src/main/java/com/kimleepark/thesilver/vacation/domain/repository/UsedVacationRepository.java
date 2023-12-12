package com.kimleepark.thesilver.vacation.domain.repository;


import com.kimleepark.thesilver.jwt.CustomUser;
import com.kimleepark.thesilver.vacation.domain.Require;
import com.kimleepark.thesilver.vacation.domain.UsedVacation;
import com.kimleepark.thesilver.vacation.domain.type.RequireStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UsedVacationRepository extends JpaRepository<Require, CustomUser> {
    Page<Require> findByEmployeeEmployeeCodeAndReqStatusAndEndDateBefore(Pageable pageable, Long employeeCode, RequireStatusType reqstatus, LocalDateTime today);
}