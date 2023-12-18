package com.kimleepark.thesilver.vacation.domain.repository;

import com.kimleepark.thesilver.vacation.domain.Require;
import com.kimleepark.thesilver.vacation.domain.Sign;
import com.kimleepark.thesilver.vacation.domain.type.SignStatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SignRepository extends JpaRepository<Sign, Long> {

    /* 관리자 페이지 - 직원 연차 관리 */
    List<Sign> findByEmployeeEmployeeCodeAndSignStatus(Long employeeCode, SignStatusType signStatus);


    Optional<Sign> findByRequireReqNo(Long reqNo);
}
