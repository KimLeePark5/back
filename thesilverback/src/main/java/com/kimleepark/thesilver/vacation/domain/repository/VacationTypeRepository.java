package com.kimleepark.thesilver.vacation.domain.repository;

import com.kimleepark.thesilver.vacation.domain.VacationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacationTypeRepository extends JpaRepository<VacationType, Long> {
}
