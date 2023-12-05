package com.kimleepark.thesilver.vacation.presentation;

import com.kimleepark.thesilver.jwt.CustomUser;
import com.kimleepark.thesilver.vacation.dto.response.VacationRequireResponse;
import com.kimleepark.thesilver.vacation.dto.response.VacationResponse;
import com.kimleepark.thesilver.vacation.service.VacationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class VacationController {

    private final VacationService vacationService;

    /* 연차 관리 - 직원 코드로 연차 현황 조회 */
    @GetMapping("/vacation")
    public ResponseEntity<VacationResponse> getVacation(@AuthenticationPrincipal CustomUser customUser) {

        Long employeeCode = customUser.getEmployeeCode();

        final VacationResponse vacationResponse = vacationService.getVacation(customUser);

        return ResponseEntity.ok(vacationResponse);
    }

    /* 연차 관리 - 상신 현황 조회 */
    @GetMapping("/require")
    public ResponseEntity<List<VacationRequireResponse>> getRequire(@AuthenticationPrincipal CustomUser customUser) {

        List<VacationRequireResponse> vacationRequireResponseList = vacationService.getRequire(customUser);

        return ResponseEntity.ok(vacationRequireResponseList);
    }



}
