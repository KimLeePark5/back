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

import java.security.SecureRandom;
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

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String ALL_CHARACTERS = ALPHABET + NUMBERS;

    public void resetPassword (ResetPasswordRequest resetPasswordRequest) {
        // 입력한 사번과 이메일 주소 검증 로직
        Employee employeeByEmail = employeeRepository.findByEmployeeEmail(resetPasswordRequest.getEmployeeEmail())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_EMPLOYEE_EMAIL));
        log.info("employee : {}", employeeByEmail);

        Account accountByEmployeeNumber = accountRepository.findByEmployeeNumber(resetPasswordRequest.getEmployeeNumber())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ACCOUNT_NUMBER));

        if(employeeByEmail.getEmployeeEmail().equals(accountByEmployeeNumber.getEmployee().getEmployeeEmail())) {
            // 임시 비밀번호 생성 및 메일 발송
            String randomPassword = generateRandomPassword(10);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(randomPassword);

            emailService.sendResetPasswordEmail(employeeByEmail.getEmployeeEmail(),randomPassword);

            accountByEmployeeNumber.updateRandomPassword(encodedPassword);
            log.info("임시 비밀번호 발송 완료 !");
        } else {
            throw new ConflictException(MISMATCH_NUMBER_EMAIL);
        }
    }

    public static String generateRandomPassword(int length) {
        StringBuilder password = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(ALL_CHARACTERS.length());
            char randomChar = ALL_CHARACTERS.charAt(randomIndex);
            password.append(randomChar);
        }

        return password.toString();
    }

}
