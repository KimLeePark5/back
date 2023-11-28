package com.kimleepark.thesilver.account.domain.repository;

import com.kimleepark.thesilver.account.domain.Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AccountRepository extends JpaRepository<Account, Long> {

    @EntityGraph(attributePaths = {"employee"})
    List<Account> findAll();

}
