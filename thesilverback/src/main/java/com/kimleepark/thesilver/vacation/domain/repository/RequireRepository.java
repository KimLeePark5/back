package com.kimleepark.thesilver.vacation.domain.repository;

import com.kimleepark.thesilver.vacation.domain.Require;
import com.kimleepark.thesilver.vacation.domain.type.RequireStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RequireRepository extends JpaRepository<Require, Long> {

    /* 사용 연차 계산하여 연차 현황에 반영하기 */
    List<Require> findByEmployeeEmployeeCodeAndReqStatus(Long employeeCode, RequireStatusType requireStatusType);

    /* 로그인 한 직원 코드로 연차 상신 현황 조회 */
    List<Require> findByEmployeeEmployeeCode(Long employeeCode);

    /* 연차 사용 리스트 조회 */
    Page<Require> findByEmployeeEmployeeCodeAndReqStatusAndEndDateBefore(Pageable pageable, Long employeeCode, RequireStatusType reqStatus, LocalDateTime today);

    /* 관리자 - 팀원들의 상신 연차 조회 */
    Page<Require> findByReqNoIn(Pageable pageable, List<Long> reqNos);


}
