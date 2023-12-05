package com.kimleepark.thesilver.vacation.domain.repository;

import com.kimleepark.thesilver.jwt.CustomUser;
import com.kimleepark.thesilver.vacation.domain.Require;

import java.util.List;

public interface UseVacationeRepository {
    List<Require> findByEmployeeEmployeeCode(CustomUser customUser);
}
