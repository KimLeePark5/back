package com.kimleepark.thesilver.employee.presentation;

import com.kimleepark.thesilver.common.paging.Pagenation;
import com.kimleepark.thesilver.common.paging.PagingButtonInfo;
import com.kimleepark.thesilver.common.paging.PagingResponse;
import com.kimleepark.thesilver.employee.dto.CustomerEmployeeResponse;
import com.kimleepark.thesilver.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/employee")
    public ResponseEntity<PagingResponse> getCustomerEmployee(@RequestParam(defaultValue = "1") final Integer page){
        final Page<CustomerEmployeeResponse> employee = employeeService.getCustomerEmployee(page);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(employee);
        final PagingResponse pagingResponse = PagingResponse.of(employee.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }
}
