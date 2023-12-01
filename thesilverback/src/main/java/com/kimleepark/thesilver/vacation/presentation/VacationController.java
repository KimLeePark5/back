package com.kimleepark.thesilver.vacation.presentation;

import com.kimleepark.thesilver.vacation.domain.type.RequireStatusType;
import com.kimleepark.thesilver.vacation.dto.response.VacationRequireResponse;
import com.kimleepark.thesilver.vacation.dto.response.VacationResponse;
import com.kimleepark.thesilver.vacation.service.VacationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("/vacation/{employeeCode}")
    public ResponseEntity<VacationResponse> getVacation(@PathVariable final Long employeeCode) {

        final VacationResponse vacationResponse = vacationService.getVacation(employeeCode);

        return ResponseEntity.ok(vacationResponse);
    }

    /* 연차 관리 - 상신 현황 조회 */
    @GetMapping("/require/{employeeCode}")
    public ResponseEntity<List<VacationRequireResponse>> getRequire(@PathVariable final Long employeeCode) {

        List<VacationRequireResponse> vacationRequireResponseList = vacationService.getRequire(employeeCode);

        return ResponseEntity.ok(vacationRequireResponseList);
    }

}
