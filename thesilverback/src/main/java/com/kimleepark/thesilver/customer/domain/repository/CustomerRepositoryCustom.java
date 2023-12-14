package com.kimleepark.thesilver.customer.domain.repository;

import com.kimleepark.thesilver.customer.dto.request.CustomerSearchRequest;
import com.kimleepark.thesilver.customer.dto.response.CustomerSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerRepositoryCustom {
    List<CustomerSearchResponse> searchCustomersByCondition(Integer page, CustomerSearchRequest condition);
    Page<CustomerSearchResponse> searchCustomersPage(Pageable pageable, CustomerSearchRequest condition);

}
