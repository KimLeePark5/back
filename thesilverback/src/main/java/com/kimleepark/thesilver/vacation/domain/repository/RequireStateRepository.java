package com.kimleepark.thesilver.vacation.domain.repository;

import com.kimleepark.thesilver.jwt.CustomUser;
import com.kimleepark.thesilver.vacation.domain.Require;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequireStateRepository extends JpaRepository<Require, CustomUser> {

    /* 직원 코드로 연차 상신 현황 조회 */
    Require findByEmployeeEmployeeCode(Long employeeCode);
}
