package com.kimleepark.thesilver.employee.service;

import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.employee.QLeaveHistory;
import com.kimleepark.thesilver.employee.dto.CustomerEmployeeResponse;
import com.kimleepark.thesilver.employee.repository.EmployeeRepository;
import com.kimleepark.thesilver.employee.repository.RankRepository;
import com.kimleepark.thesilver.employee.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;

import static com.kimleepark.thesilver.employee.QLeaveHistory.leaveHistory;
import static com.kimleepark.thesilver.employee.type.LeaveType.NO;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RankRepository rankRepository;
    private final TeamRepository teamRepository;

    @Value("${image.image-url}")
    private String IMAGE_URL;
    @Value("${image.image-dir}")
    private String IMAGE_DIR;

    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page - 1, 10, Sort.by("employeeCode").descending());
    }

    @Transactional(readOnly = true)
    public Page<CustomerEmployeeResponse> getCustomerEmployees(final Integer page){
        Page<Employee> employees = employeeRepository.findByLeave(getPageable(page), NO);

        return employees.map(employee -> CustomerEmployeeResponse.from(employee));
    }

    public CustomerEmployeeResponse getCustomerEmployee(Long employeeCodeCode) {
        Employee employee = employeeRepository.findByEmployeeCodeAndLeave(employeeCodeCode, NO);
//                .orElseThrow(() -> new NotFoundException(NOT_FOUND_PRODUCT_CODE));

        return CustomerEmployeeResponse.from(employee);
    }
}
