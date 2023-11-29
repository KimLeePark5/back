package com.kimleepark.thesilver.employee.presentation;

import com.kimleepark.thesilver.common.paging.Pagenation;
import com.kimleepark.thesilver.common.paging.PagingButtonInfo;
import com.kimleepark.thesilver.common.paging.PagingResponse;
import com.kimleepark.thesilver.employee.LeaveHistory;
import com.kimleepark.thesilver.employee.dto.CustomerEmployeeResponse;
import com.kimleepark.thesilver.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/employees")
    public ResponseEntity<PagingResponse> getCustomerEmployees(@RequestParam(defaultValue = "1") final Integer page){
        final Page<CustomerEmployeeResponse> employee = employeeService.getCustomerEmployees(page);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(employee);
        final PagingResponse pagingResponse = PagingResponse.of(employee.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    @GetMapping("/employees/{employeeCode}")
    public ResponseEntity<CustomerEmployeeResponse> getCustomerProduct(@PathVariable final Long employeeCode) {

        final CustomerEmployeeResponse customerEmployeeResponse = employeeService.getCustomerEmployee(employeeCode);

        return ResponseEntity.ok(customerEmployeeResponse);
    }
}
