package com.kimleepark.thesilver.customer.presentation;

import com.kimleepark.thesilver.common.paging.Pagenation;
import com.kimleepark.thesilver.common.paging.PagingButtonInfo;
import com.kimleepark.thesilver.common.paging.PagingResponse;
import com.kimleepark.thesilver.customer.domain.repository.CustomerRepository;
import com.kimleepark.thesilver.customer.domain.repository.LicenseRepository;
import com.kimleepark.thesilver.customer.dto.request.CreateCustomersRequest;
import com.kimleepark.thesilver.customer.dto.request.CreateLicensesRequest;
import com.kimleepark.thesilver.customer.dto.request.CustomerSearchRequest;
import com.kimleepark.thesilver.customer.dto.request.UpdateCustomersRequest;
import com.kimleepark.thesilver.customer.dto.response.CustomerMainResponse;
import com.kimleepark.thesilver.customer.dto.response.CustomerResponse;
import com.kimleepark.thesilver.customer.dto.response.CustomerSearchResponse;
import com.kimleepark.thesilver.customer.dto.response.LicensesResponse;
import com.kimleepark.thesilver.customer.service.CustomerService;
import com.kimleepark.thesilver.jwt.CustomUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    final CustomerRepository customerRepository;

    @GetMapping("/customers")
    public ResponseEntity<PagingResponse> getCustomers(@RequestParam(defaultValue = "1") final Integer page) {

        final Page<CustomerMainResponse> customerList = customerService.getCustomers(page);
        log.info("페이지커스토머리스트 : {}", customerList.getContent());
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(customerList);
        final PagingResponse pagingResponse = PagingResponse.of(customerList.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);

    }

    @GetMapping("/customers/condition")
    public ResponseEntity<PagingResponse> getCustomers(
            @RequestParam(defaultValue = "1") final Integer page,
            @RequestParam final String searchType,
            @RequestParam final String searchContent) {

        CustomerSearchRequest customerSearchRequest = CustomerSearchRequest.of(searchType,searchContent);
        log.info("customerSearchRequest : {}",customerSearchRequest);
        Page<CustomerSearchResponse> customersResult = customerService.getCustomersBySearch(page, customerSearchRequest);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(customersResult);
        final PagingResponse pagingResponse = PagingResponse.of(customersResult.getContent(), pagingButtonInfo);

//
//        log.info("customers리저트 : {}", customersResult);

        return ResponseEntity.ok(pagingResponse);
    }

    @GetMapping("/customers/{customerCode}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable final Long customerCode) {

        CustomerResponse customer = customerService.getCustomer(customerCode);

        return ResponseEntity.ok(customer);
    }

    @GetMapping("/customers/licenses/{customerCode}")
    public ResponseEntity<PagingResponse> getLicenses(@PathVariable Long customerCode, @RequestParam(defaultValue = "1") Integer page) {
        Page<LicensesResponse> licenses = customerService.getLicenses(customerCode, page);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(licenses);
        final PagingResponse pagingResponse = PagingResponse.of(licenses.getContent(), pagingButtonInfo);
        return ResponseEntity.ok(pagingResponse);
    }

    @PostMapping("/customers")
    public ResponseEntity<Void> save(@AuthenticationPrincipal CustomUser customUser,
                                     @RequestBody @Valid CreateCustomersRequest createCustomersRequest) {

        System.out.println("createCustomersRequest : " + createCustomersRequest.getMemo());
        Long customerCode = customerService.save(customUser.getEmployeeCode(), createCustomersRequest);
        System.out.println("customerCode : " + customerCode);

        return ResponseEntity.created(URI.create("/customers/" + customerCode)).build();
    }

    @PutMapping("/customers/{customerCode}")
    public ResponseEntity<Void> update(@PathVariable Long customerCode,
                                       @RequestBody @Valid UpdateCustomersRequest updateCustomersRequest) {
        customerService.update(customerCode, updateCustomersRequest);

        return ResponseEntity.created(URI.create("/customers/" + customerCode)).build();
    }

    @PostMapping("/customers/licenses/{customerCode}")
    public ResponseEntity<Void> saveLicenses(@PathVariable Long customerCode,
                                             @RequestBody @Valid CreateLicensesRequest createLicensesRequest) {
        Long licenseCode = customerService.saveLicenses(customerCode, createLicensesRequest);
        System.out.println("customerCode : " + licenseCode);

        return ResponseEntity.created(URI.create("/customers/" + licenseCode)).build();
    }

    @DeleteMapping("/customers/{customerCode}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerCode) {
        customerService.deleteCustomer(customerCode);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/customers/licenses/{licenseCode}")
    public ResponseEntity<Void> deleteLicense(@PathVariable Long licenseCode) {
        customerService.deleteLicense(licenseCode);

        return ResponseEntity.noContent().build();
    }
}
