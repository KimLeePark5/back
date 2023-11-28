package com.kimleepark.thesilver.vacation.service;

import com.kimleepark.thesilver.vacation.domain.Vacation;
import com.kimleepark.thesilver.vacation.domain.repository.VacationRepository;
import com.kimleepark.thesilver.vacation.dto.response.VacationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class VacationService {

    private final VacationRepository vacationRepository;

    /* 연차 현황 조회 - employeeCode로 각 직원의 연차 현황 조회 가능 */
    @Transactional(readOnly = true)
    public VacationResponse getVacation(final Long employeeCode) {

        Vacation vacation = vacationRepository.findByEmployeeEmployeeCode(employeeCode);

        return VacationResponse.from(vacation);
    }
}
