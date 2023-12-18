package com.kimleepark.thesilver.board.program.domain.repository;

import com.kimleepark.thesilver.board.journal.domain.Journal;
import com.kimleepark.thesilver.board.program.domain.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProgramRepository extends JpaRepository<Program, Long> {


    Page<Program> findAll(Pageable pageable);

    Page<Program> findByCategory_CategoryNameContaining(String categoryName, Pageable pageable);
    
    Program findByCode(Long categoryCode);

    Optional<Object> findByCategoryCategoryNameAndRound(String categoryName, String round);

    @Query("SELECT DISTINCT p.category.categoryName FROM Program p")
    List<String> findAllCategoryNames();


}

