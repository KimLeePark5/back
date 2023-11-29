package com.kimleepark.thesilver.account.presentation;

import com.kimleepark.thesilver.account.domain.Account;
import com.kimleepark.thesilver.account.domain.repository.AccountRepository;
import com.kimleepark.thesilver.jwt.CustomUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class AccountController {

    final AccountRepository accountRepository;

    @GetMapping("/accounts")
    public String getAccounts() {
        List<Account> result = accountRepository.findAll();
        System.out.println("result : " + result.get(1).getEmployee().getTeam().getTeamName());

        // BCryptPasswordEncoder 생성
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 비밀번호 해싱
        String hashedPassword = encoder.encode("1234");

        // 해싱된 비밀번호 출력
        System.out.println("Hashed Password: " + hashedPassword);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("프린시팔 getPrincipal {}", authentication.getPrincipal());
        log.info("프린시팔 getCredentials {}", authentication.getCredentials());
        log.info("프린시팔 getAuthorities {}", authentication.getAuthorities());
        log.info("프린시팔 getDetails {}", authentication.getDetails());
        log.info("프린시팔 getName {}", authentication.getName());
        log.info("프린시팔 getClass {}", authentication.getClass());

//        CustomUser customUser = (CustomUser) authentication.getPrincipal();
//        log.info("프린시팔 customUser {}", customUser);
//
//        log.info("프린시팔 customUser {}", customUser.getEmployeeCode());




        return "hi";
    }

}
