package com.kimleepark.thesilver.customer.domain.repository;


import com.kimleepark.thesilver.customer.domain.Customer;
import com.kimleepark.thesilver.customer.domain.type.CustomerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerRepositoryCustom {
    Page<Customer> findByStatus(Pageable pageable, CustomerStatus status);
    Optional<Customer> findByCustomerCode(Long customerCode);

    Optional<Object> findByName(String name);


}
