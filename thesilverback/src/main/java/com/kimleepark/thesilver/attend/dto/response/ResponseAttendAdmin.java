package com.kimleepark.thesilver.attend.dto.response;

import com.kimleepark.thesilver.attend.domain.Attend;
import com.kimleepark.thesilver.attend.domain.ModifiedAttend;
import com.kimleepark.thesilver.attend.dto.ResponseModifiedAttend;
import com.kimleepark.thesilver.employee.Employee;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@RequiredArgsConstructor
public class ResponseAttendAdmin {
    private final Long empCode;
    private final String empName;
    private final List<ResponseAttend> attendList;

    public static ResponseAttendAdmin from(Employee employee) {
        return new ResponseAttendAdmin(
                employee.getEmployeeCode(),
                employee.getEmployeeName(),
                employee.getAttendList().stream().map(attend -> ResponseAttend.from(attend, employee.getEmployeeCode())).collect(Collectors.toList())
        );
    }
}
