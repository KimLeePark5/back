package com.kimleepark.thesilver.vacation.service;

import com.kimleepark.thesilver.jwt.CustomUser;
import com.kimleepark.thesilver.vacation.domain.Require;
import com.kimleepark.thesilver.vacation.domain.Vacation;
import com.kimleepark.thesilver.vacation.domain.repository.RequireRepository;
import com.kimleepark.thesilver.vacation.domain.repository.VacationRepository;
import com.kimleepark.thesilver.vacation.domain.type.RequireStatusType;
import com.kimleepark.thesilver.vacation.dto.response.VacationRequireResponse;
import com.kimleepark.thesilver.vacation.dto.response.VacationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.kimleepark.thesilver.vacation.domain.type.RequireStatusType.*;

@Service
@RequiredArgsConstructor
@Transactional
public class VacationService {

    private final VacationRepository vacationRepository;
    private final RequireRepository requireRepository;

    /* 연차 현황 조회 - 로그인 한 직원의 연차 현황 조회 */
    @Transactional(readOnly = true)
    public VacationResponse getVacation(CustomUser customUser) {

        Vacation vacation = vacationRepository.findByEmployeeEmployeeCode(customUser);

        return VacationResponse.from(vacation);
    }

    /* 상신 현황 조회 - 로그인 한 직원의 연차 상신 현황 조회 */
    public List<VacationRequireResponse> getRequire(CustomUser customUser) {

        List<Require> requireList = requireRepository.findByEmployeeEmployeeCode(customUser);

        return  requireList.stream()
                .map(VacationRequireResponse::from)
                .collect(Collectors.toList());
    }
}
