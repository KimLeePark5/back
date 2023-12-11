package com.kimleepark.thesilver.login.service;

import com.kimleepark.thesilver.account.domain.Account;
import com.kimleepark.thesilver.account.domain.repository.AccountRepository;
import com.kimleepark.thesilver.common.exception.ConflictException;
import com.kimleepark.thesilver.common.exception.NotFoundException;
import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.employee.Rank;
import com.kimleepark.thesilver.employee.repository.EmployeeRepository;
import com.kimleepark.thesilver.employee.type.LeaveType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.kimleepark.thesilver.common.exception.type.ExceptionCode.*;
import static com.kimleepark.thesilver.employee.type.LeaveType.NO;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String employeeNumber) throws UsernameNotFoundException {

        Account account = accountRepository.findByEmployeeNumber(employeeNumber)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_EMPLOYEE_NUMBER));

        if (account.getAttemptCount() >= 5) {
            throw new ConflictException(MANY_LOGIN_ATTEMPTS);
        }

        String employeeRank = account.getEmployee().getRank().getRankName();

        if (!employeeRank.equals("센터장")) {
            account.increaseAttemptCount();
        }


        Long employeeCode = account.getEmployee().getEmployeeCode();
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode).orElseThrow();

        return User.builder()
                .username(account.getEmployeeNumber())
                .password(account.getEmployeePassword())
                .roles(employee.getRank().getRankName())
                .build();
    }

    public void resetAttemptCount(String userName) {
        Account account = accountRepository.findByEmployeeNumber(userName).get();
        account.resetAttemptCount();
    }


}
