package com.kimleepark.thesilver.vacation.service;

import com.kimleepark.thesilver.jwt.CustomUser;
import com.kimleepark.thesilver.vacation.domain.Require;
import com.kimleepark.thesilver.vacation.dto.response.UsedVacationListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kimleepark.thesilver.vacation.domain.type.RequireStatusType.PASS;

@Service
@RequiredArgsConstructor
@Transactional
public class VacationService {


    private final UsedVacationRepository usedVacationRepository;

    private Pageable getPageable(Integer page) {
        return PageRequest.of(page - 1, 5, Sort.by("startDate").descending());
    }

    /* 연차 사용 리스트 조회 */
    @Transactional(readOnly = true)
    public Page<UsedVacationListResponse> getUsedVacations(final Integer page, final CustomUser customUser) {
        Page<Require> requires = usedVacationRepository.findByEmployeeEmployeeCodeAndReqStatus(getPageable(page), customUser.getEmployeeCode(), PASS);

        return requires.map(require -> UsedVacationListResponse.from(require));
    }
}

