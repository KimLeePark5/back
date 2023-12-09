package com.kimleepark.thesilver.vacation.service;

import com.kimleepark.thesilver.jwt.CustomUser;
import com.kimleepark.thesilver.vacation.domain.Require;
import com.kimleepark.thesilver.vacation.domain.repository.RequireStateRepository;
import com.kimleepark.thesilver.vacation.domain.repository.UsedVacationRepository;
import com.kimleepark.thesilver.vacation.domain.repository.VacationRepository;
import com.kimleepark.thesilver.vacation.domain.type.RequireStatusType;
import com.kimleepark.thesilver.vacation.dto.response.UsedVacationListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.kimleepark.thesilver.vacation.domain.type.RequireStatusType.PASS;

@Service
@RequiredArgsConstructor
@Transactional
public class VacationService {


    private final UsedVacationRepository usedVacationRepository;

    private Pageable getPageable(Integer page) {
        return PageRequest.of(page - 1, 5, Sort.by("reqDate").descending());
    }

    /* 연차 사용 리스트 조회 */
    @Transactional(readOnly = true)
    public Page<UsedVacationListResponse> getUsedVacations(final Integer page, final CustomUser customUser) {

        LocalDateTime today = LocalDateTime.now();
        Page<Require> requires = usedVacationRepository.findByEmployeeEmployeeCodeAndReqStatusAndEndDateBefore(getPageable(page), customUser.getEmployeeCode(), PASS, today);

        return requires.map(require -> UsedVacationListResponse.from(require));
    }
}