package com.kimleepark.thesilver.customer.service;

import com.kimleepark.thesilver.common.exception.NotFoundException;
import com.kimleepark.thesilver.customer.domain.Customer;
import com.kimleepark.thesilver.customer.domain.License;
import com.kimleepark.thesilver.customer.domain.repository.CustomerRepository;
import com.kimleepark.thesilver.customer.domain.repository.LicenseRepository;
import com.kimleepark.thesilver.customer.dto.graphData.FirstGraphData;
import com.kimleepark.thesilver.customer.dto.graphData.SecondGraphData;
import com.kimleepark.thesilver.customer.dto.graphData.ThirdGraphData;
import com.kimleepark.thesilver.customer.dto.request.CreateCustomersRequest;
import com.kimleepark.thesilver.customer.dto.request.CreateLicensesRequest;
import com.kimleepark.thesilver.customer.dto.request.CustomerSearchRequest;
import com.kimleepark.thesilver.customer.dto.request.UpdateCustomersRequest;
import com.kimleepark.thesilver.customer.dto.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        return PageRequest.of(page - 1, 5, Sort.by("startDate").descending());
    }
    private Pageable getPageableCustomers(final Integer page) {
        return PageRequest.of(page - 1, 5, Sort.by("customerCode").descending());
    }



    public Page<CustomerMainResponse> getCustomers (final Integer page) {
        Page<Customer> customers = customerRepository.findByStatus(getPageable(page), ACTIVE);
        log.info("커스토머스 {}", customers);
        return customers.map(customer -> CustomerMainResponse.from(customer));
    }

    //     쿼리dsl 활용 코드
    public Page<CustomerSearchResponse> getCustomersBySearch (Integer page, CustomerSearchRequest customerSearchRequest) {
        Page<CustomerSearchResponse> customersResult = customerRepository.searchCustomersPage(getPageableCustomers(page), customerSearchRequest);
        return customersResult;
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

    public void update(Long employeeCode, Long customerCode, UpdateCustomersRequest updateCustomersRequest) {
        Customer findCustomer = customerRepository.findById(customerCode).orElseThrow();
        findCustomer.update(employeeCode, updateCustomersRequest);
    }

    public Long saveLicenses(Long employeeCode, Long customerCode, CreateLicensesRequest createLicensesRequest) {

        Customer customer = customerRepository.findById(customerCode).orElseThrow();
        License newLicense = License.of(employeeCode, customer, createLicensesRequest);

        License license = licenseRepository.save(newLicense);
        return license.getLicenseCode();
    }

    public void deleteCustomer(Long customerCode) {
        customerRepository.deleteById(customerCode);
    }

    public void deleteLicense(Long licenseCode) {
        licenseRepository.deleteById(licenseCode);
    }


//     그래프 데이터 가공
    public CustomerGraphResponse getCustomersGraphData() {

        // Second Graph Data
        List<SecondGraphData> secondGraphData = customerRepository.getSecondGraphData();

        // Third Graph Data
        List<ThirdGraphData> thirdGraphData = customerRepository.getThirdGraphData();

        // First Graph Data
        List<FirstGraphData> firstGraphData = customerRepository.getFirstGraphData();

        return CustomerGraphResponse.from(firstGraphData, secondGraphData,thirdGraphData);

    }
}
