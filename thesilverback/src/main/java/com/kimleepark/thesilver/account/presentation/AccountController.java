package com.kimleepark.thesilver.account.presentation;

import com.kimleepark.thesilver.account.domain.Account;
import com.kimleepark.thesilver.account.domain.repository.AccountRepository;
import com.kimleepark.thesilver.account.dto.request.ResetPasswordRequest;
import com.kimleepark.thesilver.account.service.AccountService;
import com.kimleepark.thesilver.jwt.CustomUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class AccountController {

    private final AccountRepository accountRepository;
    private final AccountService accountService;

    @GetMapping("/accounts")
    public String getAccounts(@AuthenticationPrincipal CustomUser customUser) {
        List<Account> result = accountRepository.findAll();
        System.out.println("result : " + result.get(1).getEmployee().getTeam().getTeamName());

        // BCryptPasswordEncoder 생성
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 비밀번호 해싱
        String hashedPassword = encoder.encode("1234");

        // 해싱된 비밀번호 출력
        System.out.println("Hashed Password: " + hashedPassword);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("userDetails : {}", customUser.getEmployeeCode());

        return "hi";
    }

    @PostMapping("/password-reset")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid final ResetPasswordRequest resetPasswordRequest) {
        log.info("resetPasswordRequest : {}", resetPasswordRequest.getEmployeeNumber());
        log.info("resetPasswordRequest : {}", resetPasswordRequest.getEmployeeEmail());
        accountService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok().build();
    }
}
