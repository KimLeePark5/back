package com.kimleepark.thesilver.vacation.service;

import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.employee.repository.EmployeeRepository;
import com.kimleepark.thesilver.jwt.CustomUser;
import com.kimleepark.thesilver.vacation.domain.Require;

import com.kimleepark.thesilver.vacation.domain.Sign;
import com.kimleepark.thesilver.vacation.domain.Vacation;
import com.kimleepark.thesilver.vacation.domain.VacationType;
import com.kimleepark.thesilver.vacation.domain.repository.*;
import com.kimleepark.thesilver.vacation.domain.type.RequireStatusType;
import com.kimleepark.thesilver.vacation.dto.response.RequireStateAdminResponse;

import com.kimleepark.thesilver.vacation.dto.response.UsedVacationListResponse;
import com.kimleepark.thesilver.vacation.dto.request.CreateRequireRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import static com.kimleepark.thesilver.vacation.domain.QRequire.require;
import static com.kimleepark.thesilver.vacation.domain.QVacation.vacation;
import static com.kimleepark.thesilver.vacation.domain.type.RequireStatusType.PASS;
import static com.kimleepark.thesilver.vacation.domain.type.RequireStatusType.PROCEED;


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

//    @Transactional(readOnly = true)
//    public Long getPassedRequireCount(final CustomUser customUser) {
//        // 'PASS' 상태인 휴가 요청의 갯수 조회
//        return requireRepository.countByEmployeeEmployeeCodeAndReqStatus(customUser.getEmployeeCode(), "PASS");
//    }

    /* 연차 상신 */
    public void save(CreateRequireRequest createRequireRequest, CustomUser customUser) { //등록
        // 로그인한 사용자의 정보를 가져옵니다.
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
        Sign newSign = Sign.of(savedRequire, employee);
        signRepository.save(newSign);
    }


    /* 연차 사용 리스트 조회 */
    @Transactional(readOnly = true)
    public Page<UsedVacationListResponse> getUsedVacation(final Integer page, final CustomUser customUser) {

        LocalDateTime today = LocalDateTime.now();
        Page<Require> requires = requireRepository.findByEmployeeEmployeeCodeAndReqStatusAndEndDateBefore(getPageable(page), customUser.getEmployeeCode(), PASS, today);

        return requires.map(require -> UsedVacationListResponse.from(require));
    }


    /* 직원 상신 현황 조회 - 관리자 */
    /* ***************수정해야함****************** */
    @Transactional(readOnly = true)
    public Page<RequireStateAdminResponse> getTeamRequires(final Integer page, final CustomUser customUser) {


        Long teamCode = employeeRepository.findByEmployeeCode(customUser.getEmployeeCode()).get().getTeam().getTeamCode();

        log.info("로그인한 사람 팀코드가 뭔가요?? : " + teamCode);

        // customUser와 teamCode가 같으면서 employeeCode가 다른 require 가져오기
        Page<Require> requires = requireRepository.findByEmployeeTeamTeamCodeAndEmployeeEmployeeCodeNotAndReqStatus(
                getPageable(page),  customUser.getEmployeeCode(), teamCode, PROCEED);

        log.info("require 데이터가 있나요?? : " + requires);

        // 가져온 require를 RequireStateAdminResponse 변환하여 반환
        return requires.map(require -> RequireStateAdminResponse.from(require));
    }



}
