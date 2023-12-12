package com.kimleepark.thesilver.account.domain.repository;

import com.kimleepark.thesilver.account.domain.Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, Long> {

    @EntityGraph(attributePaths = {"employee"})
    List<Account> findAll();

    Optional<Account> findByEmployeeNumber(String employeeNumber);

    Optional<Account> findByRefreshToken(String refreshToken);

}
