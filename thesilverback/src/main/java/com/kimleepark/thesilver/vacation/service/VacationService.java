package com.kimleepark.thesilver.vacation.service;

import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.employee.repository.EmployeeRepository;
import com.kimleepark.thesilver.jwt.CustomUser;
import com.kimleepark.thesilver.vacation.domain.Require;

import com.kimleepark.thesilver.vacation.domain.Sign;
import com.kimleepark.thesilver.vacation.domain.Vacation;
import com.kimleepark.thesilver.vacation.domain.VacationType;
import com.kimleepark.thesilver.vacation.domain.repository.*;
import com.kimleepark.thesilver.vacation.domain.type.SignStatusType;
import com.kimleepark.thesilver.vacation.dto.request.UpdateRequireRequest;
import com.kimleepark.thesilver.vacation.dto.response.RequireStateAdminResponse;

import com.kimleepark.thesilver.vacation.dto.response.UsedVacationListResponse;
import com.kimleepark.thesilver.vacation.dto.request.CreateRequireRequest;
import com.kimleepark.thesilver.vacation.dto.response.VacationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    private final VacationRepository vacationRepository;
    private final VacationTypeRepository vacationTypeRepository;
    private final EmployeeRepository employeeRepository;
    private final SignRepository signRepository;

    private Pageable getPageable(Integer page) {
        return PageRequest.of(page - 1, 5, Sort.by("reqDate").descending());
    }

    /* 연차 카운트 */
    @Transactional(readOnly = true)
    public VacationResponse getVacation(CustomUser customUser) {

        // 연차 정보 확인
        Vacation vacation = vacationRepository.findByEmployeeEmployeeCode(customUser.getEmployeeCode());


        // 'PASS' 상태인 휴가 요청의 일수 카운트
        List<Require> passedRequires = requireRepository.findByEmployeeEmployeeCodeAndReqStatus(customUser.getEmployeeCode(), PASS);

        // 카운트를 저장할 변수 초기화
        double passedReqCount = 0.0;

        // 'PASS' 상태인 요청들의 endDate - startDate 일수를 카운트
        for (Require require : passedRequires) {
            // 상신건들의 endDate - startDate 일수를 계산하여 더함
            double days = ChronoUnit.DAYS.between(require.getStartDate(), require.getEndDate()) + 1; //일수 카운트를 위해 1을 더해줌

            // vacationTypeCode에 따라 카운트를 다르게 함
            switch (require.getVacationType().getVacationTypeCode().intValue()) {
                case 1:
                    passedReqCount += days;
                    break;
                case 2:
                case 3:
                    passedReqCount += 0.5; //반차는 1일을 넘어가지 않기 때뭄ㄴ에 endDate-startDate = 0에 반차인 0.5만 더해줌
                    break;
                case 4:
                    // vacationTypeCode가 4인 경우는 더하지 않음
                    break;
                default:
                    break;
            }
        }

        //로그인 유저
        Employee loginUser = employeeRepository.getReferenceById(customUser.getEmployeeCode());

        // 결재자 정보
        Employee approver = null;

        if (loginUser.getRank().getRankCode() == 3) {
            approver = employeeRepository.findByTeamAndRankRankCode(loginUser.getTeam(), 2L);
        } else {
            approver = employeeRepository.findByRankRankCode(1L);
        }


        return VacationResponse.from(vacation, passedReqCount, customUser, approver);
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

        log.info("확인확인 : {}", createRequireRequest.getApproverCode());

        // 상신 대상자
        Employee employee = employeeRepository.getReferenceById(createRequireRequest.getApproverCode());

        log.info("확인확인3 : {}", newRequire);
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

        List<Sign> signs;

        if (signStatus.equals(PROCEED.getValue())) {
            signs = signRepository.findByEmployeeEmployeeCodeAndSignStatus(customUser.getEmployeeCode(), PROCEED);
        } else if (signStatus.equals(SignStatusType.PASS.getValue())) {
            signs = signRepository.findByEmployeeEmployeeCodeAndSignStatus(customUser.getEmployeeCode(), SignStatusType.PASS);
        } else if (signStatus.equals(RETURN.getValue())) {
            signs = signRepository.findByEmployeeEmployeeCodeAndSignStatus(customUser.getEmployeeCode(), RETURN);
        } else {
            return null;
        }

        List<Long> reqNos = signs.stream()
                .map(s -> s.getRequire().getReqNo())
                .collect(Collectors.toList());

        System.out.println("getTeamRequires : " + reqNos.size());

        Page<Require> requires = requireRepository.findByReqNoIn(getPageable(page), reqNos);

        return requires.map(require -> RequireStateAdminResponse.from(require));

    }

    /* 연차 결재 */
    @Transactional
    public void updatePass(final Long reqNo) {

        log.info("updatePass : {}", reqNo);

        Require require = requireRepository.findById(reqNo).orElseThrow();
        Sign sign = signRepository.findByRequireReqNo(reqNo).orElseThrow();

        require.updatePass();
        sign.updatePass();

        log.info("updatePass R : {}", require);
        log.info("updatePass S : {}", sign);


        requireRepository.save(require);
        signRepository.save(sign);
    }

    /* 연차 반려 */
    @Transactional
    public void updateReturn(final Long reqNo, UpdateRequireRequest updateRequireRequest) {

        log.info("updateReturn : {}", reqNo);

        Require require = requireRepository.findById(reqNo).orElseThrow();
        Sign sign = signRepository.findByRequireReqNo(reqNo).orElseThrow();

        require.updateReturn();
        sign.updateReturn(updateRequireRequest.getCause());

        log.info("updateReturn R : {}", require);
        log.info("updateReturn S : {}", sign);


        requireRepository.save(require);
        signRepository.save(sign);
    }

    /* 연차 취소 */
    @Transactional
    public void updateCancel(final Long reqNo, UpdateRequireRequest updateRequireRequest) {

        log.info("updateCancel : {}", reqNo);

        Require require = requireRepository.findById(reqNo).orElseThrow();
        Sign sign = signRepository.findByRequireReqNo(reqNo).orElseThrow();

        require.updateCancel();
        sign.updateCancel(updateRequireRequest.getCause());

        log.info("updateCancel R : {}", require);
        log.info("updateCancel S : {}", sign);


        requireRepository.save(require);
        signRepository.save(sign);
    }

}

