package com.kimleepark.thesilver.board.journal.domain.repository;

import com.kimleepark.thesilver.board.journal.domain.Journal;
import com.kimleepark.thesilver.board.program.domain.ProgramCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface JournalRepository extends JpaRepository<Journal, Long> {

    Page<Journal> findAllBy(Pageable pageable);

    Page<Journal> findAll(Specification<Journal> spec, Pageable pageable);

    Optional<Journal> findByJournalCode(Long journalCode);

    // 참관 날짜와 프로그램 카테고리에 해당하는 일지 조회
    Optional<Journal> findByObservationAndProgramCategoryCategoryNameAndProgramRound(LocalDate observation, String categoryName, String round);
}
