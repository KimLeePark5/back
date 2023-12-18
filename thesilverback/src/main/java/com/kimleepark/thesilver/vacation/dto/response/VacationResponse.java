package com.kimleepark.thesilver.vacation.dto.response;

import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.jwt.CustomUser;
import com.kimleepark.thesilver.vacation.domain.Vacation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class VacationResponse {

    private final String employeeName;
    private final Long occurVacation;
    private final Long useVacation;
    private final Long remainingVacation;
    private final String rank;
    private final Long approverCode;
    private final String approverName;



    public static VacationResponse from(final Vacation vacation, Long passedReqCount, CustomUser customUser, Employee approver){

        return new VacationResponse(
                vacation.getEmployee().getEmployeeName(),
                vacation.getOccurVacation(),
                passedReqCount, // reqStatus가 PASS 상태인 요청의 갯수
                vacation.getOccurVacation() - vacation.getUseVacation(),
                vacation.getEmployee().getRank().getRankName(),
                approver.getRank().getRankCode(),
                approver.getEmployeeName()

        );
    }

}
