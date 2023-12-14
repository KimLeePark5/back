package com.kimleepark.thesilver.vacation.service;

import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.employee.repository.EmployeeRepository;
import com.kimleepark.thesilver.jwt.CustomUser;
import com.kimleepark.thesilver.vacation.domain.Require;

import com.kimleepark.thesilver.vacation.domain.Sign;
import com.kimleepark.thesilver.vacation.domain.VacationType;
import com.kimleepark.thesilver.vacation.domain.repository.*;
import com.kimleepark.thesilver.vacation.domain.type.SignStatusType;
import com.kimleepark.thesilver.vacation.dto.response.RequireStateAdminResponse;

import com.kimleepark.thesilver.vacation.dto.response.UsedVacationListResponse;
import com.kimleepark.thesilver.vacation.dto.request.CreateRequireRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.kimleepark.thesilver.vacation.domain.type.RequireStatusType.PASS;
import static com.kimleepark.thesilver.vacation.domain.type.SignStatusType.PROCEED;
import static com.kimleepark.thesilver.vacation.domain.type.SignStatusType.RETURN;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VacationService {


    private final RequireRepository requireRepository;
    private final VacationTypeRepository vacationTypeRepository;
    private final EmployeeRepository employeeRepository;
    private final SignRepository signRepository;

    private Pageable getPageable(Integer page) {
        return PageRequest.of(page - 1, 5, Sort.by("reqDate").descending());
    }

    @Transactional(readOnly = true)
    public Long getPassedRequireCount(final CustomUser customUser) {
        // 'PASS' 상태인 휴가 요청의 갯수 조회
        return requireRepository.countByEmployeeEmployeeCodeAndReqStatus(customUser.getEmployeeCode(), PASS);
    }

    /* 연차 상신 */
    @Transactional
    public void save(CreateRequireRequest createRequireRequest, CustomUser customUser) { //등록
        // 로그인한 사용자의 정보 가져오기
        Employee loggedInEmployee = employeeRepository.getReferenceById(customUser.getEmployeeCode());
        VacationType vacationType = vacationTypeRepository.getReferenceById(createRequireRequest.getVacationTypeCode());
        Require newRequire = Require.of(
                vacationType,
                loggedInEmployee,
                createRequireRequest.getStartDate(),
                createRequireRequest.getEndDate(),
                createRequireRequest.getReqContent(),
                createRequireRequest.getReqStatus()
        );
        Require savedRequire = requireRepository.save(newRequire);
        // 상신 대상자
        Employee employee = employeeRepository.getReferenceById(createRequireRequest.getApproverCode());
        Sign newSign = Sign.of(savedRequire, employee, PROCEED);
        signRepository.save(newSign);
    }


    /* 연차 사용 리스트 조회 */
    @Transactional(readOnly = true)
    public Page<UsedVacationListResponse> getUsedVacation(final Integer page, final CustomUser customUser) {

        LocalDateTime today = LocalDateTime.now();
        Page<Require> requires = requireRepository.findByEmployeeEmployeeCodeAndReqStatusAndEndDateBefore(getPageable(page), customUser.getEmployeeCode(), PASS, today);

        return requires.map(require -> UsedVacationListResponse.from(require));
    }


    /* 상신 리스트 조회 - 관리자 */
    @Transactional(readOnly = true)
    public Page<RequireStateAdminResponse> getTeamRequires(final Integer page, final CustomUser customUser, String signStatus) {
        // signs 테이블에서 requireNo를 get. (customUser.getEmployeeCode, PROCEED)
//        String signStatus = "PROCEED";
        List<Sign> signs;

        if( signStatus.equals(PROCEED.getValue())){
           signs = signRepository.findByEmployeeEmployeeCodeAndSignStatus(customUser.getEmployeeCode(), PROCEED);
        } else if (signStatus.equals(SignStatusType.PASS.getValue())) {
            signs = signRepository.findByEmployeeEmployeeCodeAndSignStatus(customUser.getEmployeeCode(), SignStatusType.PASS);
        } else if ( signStatus.equals(RETURN.getValue())){
            signs = signRepository.findByEmployeeEmployeeCodeAndSignStatus(customUser.getEmployeeCode(), RETURN);
        } else {
            return  null;
        }

        List<Long> reqNos = signs.stream()
                .map(s -> s.getRequire().getReqNo())
                .collect(Collectors.toList());

        System.out.println("getTeamRequires : " + reqNos.size());

        Page<Require> requires = requireRepository.findByReqNoIn(getPageable(page), reqNos);

        return requires.map(require -> RequireStateAdminResponse.from(require));

//        for(Sign s : signs){
//            requires = requireRepository.findAllByReqNo(getPageable(page), s.getRequire().getReqNo());
//            System.out.println("requires : " + requires);
//        }
//

    }
}
