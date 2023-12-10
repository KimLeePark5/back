package com.kimleepark.thesilver.login.service;

import com.kimleepark.thesilver.account.domain.Account;
import com.kimleepark.thesilver.account.domain.repository.AccountRepository;
import com.kimleepark.thesilver.common.exception.NotFoundException;
import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.employee.repository.EmployeeRepository;
import com.kimleepark.thesilver.employee.type.LeaveType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.kimleepark.thesilver.common.exception.type.ExceptionCode.NOT_FOUND_EMPLOYEE_NUMBER;
import static com.kimleepark.thesilver.employee.type.LeaveType.NO;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService  {

    private final AccountRepository accountRepository;
    private final EmployeeRepository employeeRepository;
    @Override
    public UserDetails loadUserByUsername(String employeeNumber) throws UsernameNotFoundException {

        Account account = accountRepository.findByEmployeeNumber(employeeNumber)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_EMPLOYEE_NUMBER));

        Long employeeCode = account.getEmployee().getEmployeeCode();

        Employee employee = employeeRepository.findByEmployeeCode(employeeCode).orElseThrow();

        return User.builder()
                .username(account.getEmployeeNumber())
                .password(account.getEmployeePassword())
                .roles(employee.getRank().getRankName())
                .build();
    }








}
