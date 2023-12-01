package com.kimleepark.thesilver.employee.presentation;

import com.kimleepark.thesilver.common.paging.Pagenation;
import com.kimleepark.thesilver.common.paging.PagingButtonInfo;
import com.kimleepark.thesilver.common.paging.PagingResponse;
import com.kimleepark.thesilver.employee.dto.request.EmployeeUpdateRequest;
import com.kimleepark.thesilver.employee.dto.request.EmployeesCreateRequest;
import com.kimleepark.thesilver.employee.dto.request.EmployeesUpdateRequest;
import com.kimleepark.thesilver.employee.dto.response.CustomerEmployeeResponse;
import com.kimleepark.thesilver.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EmployeeController {

    private final EmployeeService employeeService;

    // 직원 정보 리스트 조회
    @GetMapping("/employees")
    public ResponseEntity<PagingResponse> getCustomerEmployees(@RequestParam(defaultValue = "1") final Integer page){
        final Page<CustomerEmployeeResponse> employee = employeeService.getCustomerEmployees(page);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(employee);
        final PagingResponse pagingResponse = PagingResponse.of(employee.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    // 직원 정보 디테일
    @GetMapping("/employees/{employeeCode}")
    public ResponseEntity<CustomerEmployeeResponse> getCustomerEmployees(@PathVariable final Long employeeCode) {

        final CustomerEmployeeResponse customerEmployeeResponse = employeeService.getCustomerEmployee(employeeCode);

        return ResponseEntity.ok(customerEmployeeResponse);
    }

    //개인 정보 조회
    @GetMapping("/employee/{employeeCode}")
    public ResponseEntity<CustomerEmployeeResponse> getCustomerEmployee(@PathVariable final Long employeeCode) {

        final CustomerEmployeeResponse customerEmployeeResponse = employeeService.getCustomerEmployee(employeeCode);

        return ResponseEntity.ok(customerEmployeeResponse);
    }

    // 직원 정보 수정
    @PutMapping("/employees/{employeeCode}")
    public ResponseEntity<Void> updates(@PathVariable final Long employeeCode,
                                       @RequestPart @Valid final EmployeesUpdateRequest employeesUpdateRequest,
                                       @RequestPart(required = false) final MultipartFile employeePicture) {

        employeeService.updates(employeeCode, employeePicture, employeesUpdateRequest);

        return ResponseEntity.created(URI.create("/employees/" + employeeCode)).build();
    }

    // 개인 정보 수정
    @PutMapping("/employee/{employeeCode}")
    public ResponseEntity<Void> update(@PathVariable final Long employeeCode,
                                       @RequestBody @Valid final EmployeeUpdateRequest employeeUpdateRequest) {

        employeeService.update(employeeCode, employeeUpdateRequest);

        return ResponseEntity.created(URI.create("/employee/" + employeeCode)).build();
    }

    // 직원 정보 삭제
    @DeleteMapping("/employees/{employeeCode}")
    public ResponseEntity<Void> delete(@PathVariable final Long employeeCode) {

        employeeService.delete(employeeCode);

        return ResponseEntity.noContent().build();
    }

    // 직원 등록
    @PostMapping("/employees")
    public ResponseEntity<Void> save(@RequestPart @Valid final EmployeesCreateRequest employeesCreateRequest,
                                     @RequestPart(required = false) final MultipartFile employeePicture) {

        final Long employeeCode = employeeService.save(employeePicture, employeesCreateRequest);

        return ResponseEntity.created(URI.create("/employees/" + employeeCode)).build();
    }
}
