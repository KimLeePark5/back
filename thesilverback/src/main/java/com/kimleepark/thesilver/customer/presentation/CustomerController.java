package com.kimleepark.thesilver.customer.presentation;

import com.kimleepark.thesilver.common.paging.Pagenation;
import com.kimleepark.thesilver.common.paging.PagingButtonInfo;
import com.kimleepark.thesilver.common.paging.PagingResponse;
import com.kimleepark.thesilver.customer.domain.License;
import com.kimleepark.thesilver.customer.domain.repository.CustomerRepository;
import com.kimleepark.thesilver.customer.domain.repository.LicenseRepository;
import com.kimleepark.thesilver.customer.dto.request.CreateCustomersRequest;
import com.kimleepark.thesilver.customer.dto.request.CreateLicensesRequest;
import com.kimleepark.thesilver.customer.dto.request.UpdateCustomersRequest;
import com.kimleepark.thesilver.customer.dto.response.CustomerMainResponse;
import com.kimleepark.thesilver.customer.dto.response.CustomerResponse;
import com.kimleepark.thesilver.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CustomerController {

    final CustomerService customerService;
    final LicenseRepository licenseRepository;

    @GetMapping("/customers")
    public ResponseEntity<PagingResponse> getCustomers(@RequestParam(defaultValue = "1") final Integer page) {

        final Page<CustomerMainResponse> customerList = customerService.getCustomers(page);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(customerList);
        final PagingResponse pagingResponse = PagingResponse.of(customerList.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    @GetMapping("/customers/{customerCode}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable final Long customerCode) {

        CustomerResponse customer = customerService.getCustomer(customerCode);

        return ResponseEntity.ok(customer);
    }

    @GetMapping("/customers/licenses/{customerCode}")
    public ResponseEntity<PagingResponse> getLicenses(@PathVariable Long customerCode, @RequestParam(defaultValue = "1") Integer page) {
        Page<License> licenses = customerService.getLicenses(customerCode, page);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(licenses);
        final PagingResponse pagingResponse = PagingResponse.of(licenses.getContent(), pagingButtonInfo);
        return ResponseEntity.ok(pagingResponse);
    }

    @PostMapping("/customers")
    public ResponseEntity<Void> save(@RequestBody @Valid CreateCustomersRequest createCustomersRequest) {
        Long customerCode = customerService.save(createCustomersRequest);
        System.out.println("customerCode : " + customerCode);

        return ResponseEntity.created(URI.create("/customers/" + customerCode)).build();
    }

    @PutMapping("/customers/{customersCode}")
    public ResponseEntity<Void> update(@PathVariable Long customersCode,
                                       @RequestBody @Valid UpdateCustomersRequest updateCustomersRequest) {
        customerService.update(customersCode, updateCustomersRequest);

        return ResponseEntity.created(URI.create("/customers/" + customersCode)).build();
    }

    @PostMapping("/customers/licenses/{customersCode}")
    public ResponseEntity<Void> saveLicenses(@PathVariable Long customersCode,
                                             @RequestBody @Valid CreateLicensesRequest createLicensesRequest) {
        Long licenseCode = customerService.saveLicenses(customersCode, createLicensesRequest);
        System.out.println("customerCode : " + licenseCode);

        return ResponseEntity.created(URI.create("/customers/" + licenseCode)).build();
    }
}
