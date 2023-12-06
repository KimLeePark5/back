package com.kimleepark.thesilver.vacation.service;

import com.kimleepark.thesilver.vacation.domain.repository.RequireStateRepository;
import com.kimleepark.thesilver.vacation.domain.repository.VacationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class VacationService {

    private final VacationRepository vacationRepository;
    private final RequireStateRepository requireRepository;

    private Pageable getPageable(Integer page) {
        return PageRequest.of(page - 1, 5, Sort.by("startDate").descending());
    }

    /* 연차 현황 조회 - 로그인 한 직원의 연차 현황 조회 */
//    @Transactional(readOnly = true)
//    public VacationResponse getVacation(CustomUser customUser) {
//
//        Vacation vacation = vacationRepository.findByEmployeeEmployeeCode(customUser);
//
//        return VacationResponse.from(vacation);
//    }

    /* 상신 현황 조회 - 로그인 한 직원의 연차 상신 현황 조회 */
//    public Page<VacationRequireStateResponse> getVacationRequires(Integer page, CustomUser customUser) {
//        Pageable pageable = getPageable(page);
//        Page<Require> requires = requireRepository.findAll(pageable);
//
//        List<VacationRequireStateResponse> responseList = requires.stream()
//                .map(require -> VacationRequireStateResponse.from(require, customUser.getEmployeeCode()))
//                .collect(Collectors.toList());
//
//        return new PageImpl<>(responseList, pageable, requires.getTotalElements());
//    }



}

