package com.kimleepark.thesilver.account.domain.repository;

import com.kimleepark.thesilver.account.domain.Account;
import com.kimleepark.thesilver.account.domain.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

}
