package com.kimleepark.thesilver.customer.domain.repository;

import com.kimleepark.thesilver.customer.dto.request.CustomerSearchRequest;
import com.kimleepark.thesilver.customer.dto.response.CustomerSearchResponse;
import com.kimleepark.thesilver.customer.dto.response.QCustomerSearchResponse;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.kimleepark.thesilver.customer.domain.QCustomer.customer;


public class CustomerRepositoryImpl implements CustomerRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public CustomerRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }




    public List<CustomerSearchResponse> searchCustomersByCondition(Integer page, CustomerSearchRequest condition) {

        return queryFactory
                .select(new QCustomerSearchResponse(
                        customer.customerCode,
                        customer.name,
                        customer.gender,
                        customer.birthDate,
                        customer.primaryAddress,
                        customer.status,
                        customer.phone
                ))
                .from(customer)
                .where(
                        codeEq(condition),
                        nameLike(condition),
                        phoneLike(condition),
                        addressLike(condition)
                )
                .fetch();
    }

    // 동적 쿼리 조건 로직

    private BooleanExpression codeEq(CustomerSearchRequest condition) {
        return condition.getSearchType().equals("고객코드") ? customer.customerCode.eq(Long.parseLong(condition.getSearchContent())) : null ;
    }
    private BooleanExpression nameLike(CustomerSearchRequest condition) {
        return condition.getSearchType().equals("이름") ? customer.name.like("%"+condition.getSearchContent()+"%") : null ;
    }
    private BooleanExpression phoneLike(CustomerSearchRequest condition) {
        return condition.getSearchType().equals("전화번호") ? customer.phone.like("%"+condition.getSearchContent()+"%") : null ;
    }
    private BooleanExpression addressLike(CustomerSearchRequest condition) {
        return condition.getSearchType().equals("주소") ? customer.customerCode.like("%"+condition.getSearchContent()+"%") : null ;
    }



    @Override
    public Page<CustomerSearchResponse> searchCustomersPage(Pageable pageable, CustomerSearchRequest condition) {
        QueryResults<CustomerSearchResponse> results = queryFactory
                .select(new QCustomerSearchResponse(
                        customer.customerCode,
                        customer.name,
                        customer.gender,
                        customer.birthDate,
                        customer.primaryAddress,
                        customer.status,
                        customer.phone
                ))
                .from(customer)
                .where(
                        codeEq(condition),
                        nameLike(condition),
                        phoneLike(condition),
                        addressLike(condition)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<CustomerSearchResponse> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }
}
