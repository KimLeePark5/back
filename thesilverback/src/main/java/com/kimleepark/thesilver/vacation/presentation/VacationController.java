package com.kimleepark.thesilver.vacation.presentation;

import com.kimleepark.thesilver.common.paging.Pagenation;
import com.kimleepark.thesilver.common.paging.PagingButtonInfo;
import com.kimleepark.thesilver.common.paging.PagingResponse;

import com.kimleepark.thesilver.jwt.CustomUser;
import com.kimleepark.thesilver.vacation.domain.Require;
import com.kimleepark.thesilver.vacation.domain.Vacation;

import com.kimleepark.thesilver.vacation.domain.repository.VacationRepository;


import com.kimleepark.thesilver.vacation.domain.repository.RequireRepository;
import com.kimleepark.thesilver.vacation.domain.type.SignStatusType;
import com.kimleepark.thesilver.vacation.dto.request.CreateRequireRequest;

import com.kimleepark.thesilver.vacation.dto.response.UsedVacationListResponse;
import com.kimleepark.thesilver.vacation.dto.response.*;
import com.kimleepark.thesilver.vacation.service.VacationService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class VacationController {

    private final VacationRepository vacationRepository;
    private final RequireRepository requireRepository;
    private final VacationService vacationService;

    /* 연차 관리 - 직원 코드로 연차 현황 조회 */
    @GetMapping("/vacation")
    public ResponseEntity<VacationResponse> getVacation(@AuthenticationPrincipal CustomUser customUser) {

        System.out.println("확인" + customUser.getEmployeeName());
        // 연차 정보 확인
        Vacation vacation = vacationRepository.findByEmployeeEmployeeCode(customUser.getEmployeeCode());

        // 'PASS' 상태인 휴가 요청의 갯수 조회
        Long passedReqCount = vacationService.getPassedRequireCount(customUser);

        System.out.println("PASS상태인 휴가 요청 갯수는? : "+ passedReqCount );

        VacationResponse vacationResponse = VacationResponse.from(vacation, passedReqCount, customUser);

        log.info("customUser 권한: {}", vacationResponse.getAuthorities());

        return ResponseEntity.ok(vacationResponse);
    }


    /* 연차 상신하기 */
    @PostMapping("/require") // 등록
    public ResponseEntity<Void> save(@RequestBody @Validated CreateRequireRequest createRequireRequest,
                                     @AuthenticationPrincipal CustomUser customUser) {
        System.out.println("확인" + customUser.getEmployeeName());
       vacationService.save(createRequireRequest, customUser);


        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /* 연차 관리 - 상신 현황 조회 */
    @GetMapping("/requireState")
    public ResponseEntity<List<VacationRequireStateResponse>> getRequires(@AuthenticationPrincipal CustomUser customUser) {

        System.out.println("확인" + customUser.getEmployeeName());

        List<Require> requires = requireRepository.findByEmployeeEmployeeCode(customUser.getEmployeeCode());

        List<VacationRequireStateResponse> vacationRequireStateResponses = requires
                .stream()
                .map(VacationRequireStateResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(vacationRequireStateResponses);
    }

    /* 연차 관리 - 사용한 연차 리스트 조회 */
    @GetMapping("/usedVacation")
    public ResponseEntity<PagingResponse> getUsedVacation(@AuthenticationPrincipal CustomUser customUser,
                                                          @RequestParam(defaultValue = "1") final Integer page) {

        final Page<UsedVacationListResponse> requires = vacationService.getUsedVacation(page, customUser);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(requires);
        final PagingResponse pagingResponse = PagingResponse.of(requires.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    /* 직원 상신 현황 조회 - 관리자 */
    @GetMapping("/ProceedRequireAdmin")
    public ResponseEntity<PagingResponse> getTeamRequires(@AuthenticationPrincipal CustomUser customUser,
                                                          @RequestParam(defaultValue = "1") final Integer page,
                                                          @RequestParam final String signStatus) {

        log.info("확인 : {}", signStatus);

        final Page<RequireStateAdminResponse> requires = vacationService.getTeamRequires(page, customUser, signStatus);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(requires);
        final PagingResponse pagingResponse = PagingResponse.of(requires.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

}