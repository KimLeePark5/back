package com.kimleepark.thesilver.vacation.domain.repository;

import com.kimleepark.thesilver.jwt.CustomUser;
import com.kimleepark.thesilver.vacation.domain.Require;
import com.kimleepark.thesilver.vacation.domain.type.RequireStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RequireRepository extends JpaRepository<Require, Long> {

    /* 사용 연차 계산하여 연차 현황에 반영하기 */
    Long countByEmployeeEmployeeCodeAndReqStatus(Long employeeCode, String reqStatus);

    /* 로그인 한 직원 코드로 연차 상신 현황 조회 */
    List<Require> findByEmployeeEmployeeCode(Long employeeCode);

    Page<Require> findByEmployeeEmployeeCodeAndReqStatusAndEndDateBefore(Pageable pageable, Long employeeCode, RequireStatusType reqstatus, LocalDateTime today);

    /* 관리자 페이지 - 직원 연차 관리 */
    Page<Require> findByEmployeeTeamTeamCodeAndEmployeeEmployeeCodeNotAndReqStatus(Pageable pageable, Long employeeCode, Long teamCode, RequireStatusType requireStatusType );



}
