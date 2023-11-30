package com.kimleepark.thesilver.vacation;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class Calculator { // 입사일 기준으로 if문을 돌려서 매일 돌아가지 않고 조건이 맞을 때만 돌아가게 해야함.

        public static int calculateVacation(String joinDate, int workingDaysInMonth, LocalDate accountingYearStart) {
            // 현재 날짜를 가져오기
            LocalDate currentDate = LocalDate.now();

            // 입사일을 LocalDate 객체로 변환 1qq1`      1`  aQ1`
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate joinDateObj = LocalDate.parse(joinDate, formatter);

            // 근속 기간을 계산
            long employmentDuration = currentDate.toEpochDay() - joinDateObj.toEpochDay();

            // 회계년도의 시작일을 고려하여 연차를 리셋
            LocalDate resetDate = accountingYearStart.isAfter(joinDateObj) ? accountingYearStart : joinDateObj;

            // 근속 기간을 회계년도 단위로 조정
            long adjustedEmploymentDuration = currentDate.toEpochDay() - resetDate.toEpochDay();

            // 1년 미만인 경우
            if (adjustedEmploymentDuration < 365) {
                return workingDaysInMonth >= 25 ? 1 : 0;
            }

            // 1년 이상인 경우
            int baseAnnualVacation = 15;
            int additionalVacationPerYear = 1;
            int maxTotalVacation = 25;

            // 연차 계산
            int totalVacation = baseAnnualVacation + additionalVacationPerYear;

            while (adjustedEmploymentDuration >= 730 && totalVacation < maxTotalVacation) {
                totalVacation += additionalVacationPerYear;
                adjustedEmploymentDuration -= 730; // 2년 간격으로 추가 연차 발생
            }

            return totalVacation;
        }

        public static void main(String[] args) {
            // 입사일, 특정 달의 출근일수, 회계년도 시작일을 입력하여 연차를 계산
            String joinDate = "2022-08-01"; // 예시 입사일
            int workingDaysInMonth = 27;    // 예시 출근일수
            LocalDate accountingYearStart = LocalDate.of(2023, 1, 1); // 회계년도 시작일

            int annualVacationDays = calculateVacation(joinDate, workingDaysInMonth, accountingYearStart);
            System.out.println("연차 일수: " + annualVacationDays);
        }

}
