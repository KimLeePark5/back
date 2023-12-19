package com.kimleepark.thesilver.employee.presentation;
import com.kimleepark.thesilver.common.paging.Pagenation;
import com.kimleepark.thesilver.common.paging.PagingButtonInfo;
import com.kimleepark.thesilver.common.paging.PagingResponse;
import com.kimleepark.thesilver.employee.dto.request.EmployeeUpdateRequest;
import com.kimleepark.thesilver.employee.dto.request.EmployeesCreateRequest;
import com.kimleepark.thesilver.employee.dto.request.EmployeesUpdateRequest;
import com.kimleepark.thesilver.employee.dto.request.RankUpdateRequest;
import com.kimleepark.thesilver.employee.dto.response.CustomerEmployeesResponse;
import com.kimleepark.thesilver.employee.service.EmployeeService;
import com.kimleepark.thesilver.jwt.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        final Page<CustomerEmployeesResponse> employee = employeeService.getCustomerEmployeesManager(page);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(employee);
        final PagingResponse pagingResponse = PagingResponse.of(employee.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    // 직원 정보 디테일
    @GetMapping("/employees/{employeeCode}")
    public ResponseEntity<CustomerEmployeesResponse> getCustomerEmployees(@PathVariable final Long employeeCode) {

        final CustomerEmployeesResponse customerEmployeeResponse = employeeService.getCustomerEmployeeManager(employeeCode);

        return ResponseEntity.ok(customerEmployeeResponse);
    }

    //개인 정보 조회
    @GetMapping("/employee")
    public ResponseEntity<CustomerEmployeesResponse> getCustomerEmployees(@AuthenticationPrincipal CustomUser customUser) {

        System.out.println("화악인!~~");
        Long employeeCode = customUser.getEmployeeCode();
        final CustomerEmployeesResponse customerEmployeeResponse = employeeService.getCustomerEmployeeManager(employeeCode);

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

    //직원 등록
    @PostMapping("/employees")
    public ResponseEntity<Void> save(@RequestPart @Valid final EmployeesCreateRequest employeesCreateRequest,
                                     @RequestPart(required = false) final MultipartFile employeePicture) {


        final Long employeeCode = employeeService.save(employeePicture, employeesCreateRequest);


        return ResponseEntity.created(URI.create("/employees/" + employeeCode)).build();

    }

    @GetMapping("/employees/search")
    public ResponseEntity<PagingResponse> getCustomerEmployeesSearch(@RequestParam(defaultValue = "1") final Integer page, String searchCategory,String searchValue){
        final Page<CustomerEmployeesResponse> employeesSearch = employeeService.getCustomerEmployeesSearch(page, searchCategory, searchValue);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(employeesSearch);
        final PagingResponse pagingResponse = PagingResponse.of(employeesSearch.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    @PutMapping("/resetPwd/{employeeCode}")
    public ResponseEntity<Void> empPwdUpdate(@PathVariable final Long employeeCode) {


        employeeService.empPwdUpdate(employeeCode);

        return ResponseEntity.created(URI.create("/resetPwd/" + employeeCode)).build();
    }

     //직원 직급변경시





//    @GetMapping("/employees/search")
//    public ResponseEntity<PagingResponse> getCustomerEmployeesSearch(
//            @RequestParam String employeesSearch,
//            @RequestParam(defaultValue = "1") final Integer page,
//            @RequestParam String employeesSearchValue
//            @RequestParam final Long employeeCode,
//            @RequestParam final String rankCode,
//            @RequestParam final String teamCode,
//            @RequestParam final String employeeName,
//            @RequestParam final String gender,
//            @RequestParam final String registrationNumber,
//            @RequestParam final String employeePhone,
//            @RequestParam final LocalDateTime join_date,
//            @RequestParam final String disability,
//            @RequestParam final String marriage,
//            @RequestParam final String leave_type,
//            @RequestParam final String patriots,
//            @RequestParam final String working_status
//    ){
//        final Page<CustomerEmployeesResponse> products = employeeService.getCustomerEmployeesSearch(employeesSearch, page, employeesSearchValue);
//        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(products);
//        final PagingResponse pagingResponse = PagingResponse.of(products.getContent(), pagingButtonInfo);
//
//        return ResponseEntity.ok(pagingResponse);
//    }
}