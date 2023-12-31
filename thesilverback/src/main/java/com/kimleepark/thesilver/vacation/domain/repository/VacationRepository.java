package com.kimleepark.thesilver.vacation.domain.repository;

import com.kimleepark.thesilver.vacation.domain.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VacationRepository extends JpaRepository<Vacation, Long> {

    /* 직원 코드로 연차 현황 조회 */
    Vacation findByEmployeeEmployeeCode(Long employeeCode);

}
