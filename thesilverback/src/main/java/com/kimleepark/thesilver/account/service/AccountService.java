package com.kimleepark.thesilver.account.service;

import com.kimleepark.thesilver.account.domain.Account;
import com.kimleepark.thesilver.account.domain.repository.AccountRepository;
import com.kimleepark.thesilver.account.dto.request.ResetPasswordRequest;
import com.kimleepark.thesilver.common.exception.ConflictException;
import com.kimleepark.thesilver.common.exception.NotFoundException;
import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.kimleepark.thesilver.common.exception.type.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final EmployeeRepository employeeRepository;
    private final EmailService emailService;

    public void resetPassword (ResetPasswordRequest resetPasswordRequest) {
        // 입력한 사번과 이메일 주소 검증 로직
        Employee employeeByEmail = employeeRepository.findByEmployeeEmail(resetPasswordRequest.getEmployeeEmail())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_EMPLOYEE_EMAIL));
        log.info("employee : {}", employeeByEmail);
        Account accountByEmployeeNumber = accountRepository.findByEmployeeNumber(resetPasswordRequest.getEmployeeNumber())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ACCOUNT_NUMBER));

        if(employeeByEmail.getEmployeeEmail().equals(accountByEmployeeNumber.getEmployee().getEmployeeEmail())) {
            // 임시 비밀번호 생성 및 메일 발송
            String randomPassword = String.valueOf((int) (Math.random() * 100000));
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(randomPassword);

            emailService.sendResetPasswordEmail(employeeByEmail.getEmployeeEmail(),randomPassword);

            accountByEmployeeNumber.updateRandomPassword(encodedPassword);
            log.info("임시 비밀번호 발송 완료 !");
        } else {
            throw new ConflictException(MISMATCH_NUMBER_EMAIL);
        }



    }

}
