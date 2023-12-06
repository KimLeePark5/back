package com.kimleepark.thesilver.vacation.domain.repository;

import com.kimleepark.thesilver.jwt.CustomUser;
import com.kimleepark.thesilver.vacation.domain.Require;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequireRepository extends JpaRepository<Require, Long> {

    /* 직원 코드로 연차 상신 현황 조회 */
    List<Require> findByEmployeeEmployeeCode(CustomUser customUser);
}
