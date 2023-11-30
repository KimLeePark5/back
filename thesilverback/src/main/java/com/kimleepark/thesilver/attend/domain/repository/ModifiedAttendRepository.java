package com.kimleepark.thesilver.attend.domain.repository;

import com.kimleepark.thesilver.attend.domain.ModifiedAttend;
import com.kimleepark.thesilver.attend.dto.ResponseModifiedAttend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModifiedAttendRepository extends JpaRepository<ModifiedAttend,Long> {
    Page<ModifiedAttend> findByAttendNo(Pageable pageable, int attendNo);
}
