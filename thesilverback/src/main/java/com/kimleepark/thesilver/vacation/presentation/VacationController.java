package com.kimleepark.thesilver.vacation.presentation;

import com.kimleepark.thesilver.common.paging.Pagenation;
import com.kimleepark.thesilver.common.paging.PagingButtonInfo;
import com.kimleepark.thesilver.common.paging.PagingResponse;
import com.kimleepark.thesilver.employee.repository.EmployeeRepository;
import com.kimleepark.thesilver.jwt.CustomUser;
import com.kimleepark.thesilver.vacation.domain.Require;
import com.kimleepark.thesilver.vacation.domain.UsedVacation;
import com.kimleepark.thesilver.vacation.domain.Vacation;
import com.kimleepark.thesilver.vacation.domain.repository.RequireStateRepository;
import com.kimleepark.thesilver.vacation.domain.repository.UsedVacationRepository;
import com.kimleepark.thesilver.vacation.domain.repository.VacationRepository;
import com.kimleepark.thesilver.vacation.domain.type.RequireStatusType;
import com.kimleepark.thesilver.vacation.dto.response.UsedVacationListResponse;
import com.kimleepark.thesilver.vacation.dto.response.VacationRequireStateResponse;
import com.kimleepark.thesilver.vacation.dto.response.VacationResponse;
import com.kimleepark.thesilver.vacation.service.VacationService;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.source.internal.hbm.PluralAttributeSourceSetImpl;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.kimleepark.thesilver.vacation.domain.type.RequireStatusType.PASS;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class VacationController {

    private final VacationRepository vacationRepository;
    private final RequireStateRepository requireStateRepository;
    private final UsedVacationRepository usedVacationRepository;
    private final VacationService vacationService;

    /* 연차 관리 - 직원 코드로 연차 현황 조회 */
    @GetMapping("/vacation")
    public ResponseEntity<VacationResponse> getVacation(@AuthenticationPrincipal CustomUser customUser) {

        System.out.println("확인" + customUser.getEmployeeName());
        Vacation vacation = vacationRepository. findByEmployeeEmployeeCode(customUser.getEmployeeCode());

        VacationResponse vacationResponse = VacationResponse.from(vacation);

        System.out.println("vacationResponse : " + vacationResponse.getOccurVacation());


        return ResponseEntity.ok(vacationResponse);
    }

    /* 연차 관리 - 상신 현황 조회 */
    @GetMapping("/requireState")
    public ResponseEntity<List<VacationRequireStateResponse>> getRequires(@AuthenticationPrincipal CustomUser customUser) {

        System.out.println("확인" + customUser.getEmployeeName());

        List<Require> requires = requireStateRepository.findByEmployeeEmployeeCode(customUser.getEmployeeCode());

        List<VacationRequireStateResponse> vacationRequireStateResponses = requires
                .stream()
                .map(VacationRequireStateResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(vacationRequireStateResponses);
    }

    /* 연차 관리 - 사용한 연차 리스트 조회 */
//    @GetMapping("/usedVacation")
//    public ResponseEntity<List<UsedVacationListResponse>> getUsedVacations(@AuthenticationPrincipal CustomUser customUser) {
//
//        System.out.println("확인" + customUser.getEmployeeName());
//
//        List<Require> requires = usedVacationRepository.findByEmployeeEmployeeCodeAndReqStatus(customUser.getEmployeeCode());
//
//        List<UsedVacationListResponse> usedVacationListResponse = requires
//                .stream()
//                .map(UsedVacationListResponse::from)
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(usedVacationListResponse);
//    }

    @GetMapping("/usedVacation")
    public ResponseEntity<PagingResponse> getUsedVacation(@AuthenticationPrincipal CustomUser customUser,
                                                           @RequestParam(defaultValue = "1") final Integer page) {

        final Page<UsedVacationListResponse> requires = vacationService.getUsedVacations(page, customUser);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(requires);
        final PagingResponse pagingResponse = PagingResponse.of(requires.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }
}
