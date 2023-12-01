package com.kimleepark.thesilver.Program.domain.repository;

import com.kimleepark.thesilver.Program.domain.ProgramCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProgramCategoryRepository extends CrudRepository<ProgramCategory, Long> {

    // 카테고리 이름으로 조회하는 메서드
    Optional<ProgramCategory> findByCategoryName(String categoryName);

}
