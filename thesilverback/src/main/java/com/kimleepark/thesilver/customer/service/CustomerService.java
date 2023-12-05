package com.kimleepark.thesilver.customer.service;

import com.kimleepark.thesilver.common.exception.NotFoundException;
import com.kimleepark.thesilver.customer.domain.Customer;
import com.kimleepark.thesilver.customer.domain.License;
import com.kimleepark.thesilver.customer.domain.repository.CustomerRepository;
import com.kimleepark.thesilver.customer.domain.repository.LicenseRepository;
import com.kimleepark.thesilver.customer.domain.type.CustomerStatus;
import com.kimleepark.thesilver.customer.dto.request.CreateCustomersRequest;
import com.kimleepark.thesilver.customer.dto.request.CreateLicensesRequest;
import com.kimleepark.thesilver.customer.dto.request.UpdateCustomersRequest;
import com.kimleepark.thesilver.customer.dto.response.CustomerMainResponse;
import com.kimleepark.thesilver.customer.dto.response.CustomerResponse;
import com.kimleepark.thesilver.customer.dto.response.LicensesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.kimleepark.thesilver.common.exception.type.ExceptionCode.NOT_FOUND_CUSTOMER_CODE;
import static com.kimleepark.thesilver.customer.domain.type.CustomerStatus.ACTIVE;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CustomerService {

    final CustomerRepository customerRepository;
    final LicenseRepository licenseRepository;

    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page - 1, 5, Sort.by("customerCode").descending());
    }
    private Pageable getPageableLicense(final Integer page) {
        return PageRequest.of(page - 1, 5, Sort.by("licenseCode").descending());
    }



    public Page<CustomerMainResponse> getCustomers (final Integer page) {
        Page<Customer> customers = customerRepository.findByStatus(getPageable(page), ACTIVE);
        log.info("커스토머스 {}", customers);
        return customers.map(customer -> CustomerMainResponse.from(customer));
    }

    public CustomerResponse getCustomer (final Long customerCode) {
        Customer customer = customerRepository.findByCustomerCode(customerCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_CUSTOMER_CODE));
        CustomerResponse customerResponse = CustomerResponse.from(customer);
        return customerResponse;
    }

    public Page<LicensesResponse> getLicenses(Long customerCode,Integer page) {
        Page<License> licenses = licenseRepository.findByCustomerCustomerCode(customerCode, getPageableLicense(page));
        return licenses.map(license -> LicensesResponse.from(license));
    }

    public Long save(Long employeeCode, CreateCustomersRequest createCustomersRequest) {
        Customer newCustomer = Customer.of(employeeCode, createCustomersRequest);
        Customer customer = customerRepository.save(newCustomer);
        return customer.getCustomerCode();
    }

    public void update(Long customerCode, UpdateCustomersRequest updateCustomersRequest) {
        Customer findCustomer = customerRepository.findById(customerCode).orElseThrow();
        findCustomer.update(updateCustomersRequest);
    }

    public Long saveLicenses(Long customerCode, CreateLicensesRequest createLicensesRequest) {

        Customer customer = customerRepository.findById(customerCode).orElseThrow();
        License newLicense = License.of(customer,createLicensesRequest);

        License license = licenseRepository.save(newLicense);
        return license.getLicenseCode();
    }

    public void deleteCustomer(Long customerCode) {
        customerRepository.deleteById(customerCode);
    }

    public void deleteLicense(Long licenseCode) {
        licenseRepository.deleteById(licenseCode);
    }
}
