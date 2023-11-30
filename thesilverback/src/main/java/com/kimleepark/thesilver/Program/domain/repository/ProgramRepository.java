package com.kimleepark.thesilver.Program.domain.repository;

import com.kimleepark.thesilver.Program.domain.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProgramRepository extends JpaRepository<Program, Long> {


    Page<Program> findAll(Pageable pageable);
    Page<Program> findByCategory_CategoryNameContaining(String categoryName, Pageable pageable);
    Program findByCategory_CategoryCode(Long categoryCode);


}

