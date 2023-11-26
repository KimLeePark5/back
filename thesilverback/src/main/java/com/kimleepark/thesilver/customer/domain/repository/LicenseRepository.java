package com.kimleepark.thesilver.customer.domain.repository;

import com.kimleepark.thesilver.customer.domain.License;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

public interface LicenseRepository extends JpaRepository<License,Long> {

    @EntityGraph(attributePaths = {"customer"})
    List<License> findAll();

    @EntityGraph(attributePaths = {"customer"})
    Page<License> findByCustomerCustomerCode(Long customerCode, Pageable page);


}
