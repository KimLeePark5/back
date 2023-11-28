package com.kimleepark.thesilver.account.presentation;

import com.kimleepark.thesilver.account.domain.Account;
import com.kimleepark.thesilver.account.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        System.out.println("result : " + result);
        return "hi";
    }

}
