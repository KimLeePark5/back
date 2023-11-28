package com.kimleepark.thesilver.employee.repository;

import com.kimleepark.thesilver.employee.Rank;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankRepository extends JpaRepository<Rank, Long> {
}
