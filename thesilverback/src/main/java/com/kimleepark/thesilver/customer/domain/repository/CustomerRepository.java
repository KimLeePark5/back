package com.kimleepark.thesilver.customer.domain.repository;


import com.kimleepark.thesilver.customer.domain.Customer;
import com.kimleepark.thesilver.customer.domain.type.CustomerStatus;
import com.kimleepark.thesilver.customer.dto.graphData.FirstGraphData;
import com.kimleepark.thesilver.customer.dto.graphData.SecondGraphData;
import com.kimleepark.thesilver.customer.dto.graphData.ThirdGraphData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerRepositoryCustom {
    Page<Customer> findByStatus(Pageable pageable, CustomerStatus status);
    Optional<Customer> findByCustomerCode(Long customerCode);
    Optional<Object> findByName(String name);

    // Second Graph Data
    @Query(value =
            "SELECT " +
                    "  CASE " +
                    "    WHEN LOCATE('종로구', primary_address) THEN '종로구' " +
                    "    WHEN LOCATE('서대문구', primary_address) THEN '서대문구' " +
                    "    WHEN LOCATE('성북구', primary_address) THEN '성북구' " +
                    "    WHEN LOCATE('용산구', primary_address) THEN '용산구' " +
                    "    ELSE '기타' " +
                    "  END AS primaryAddress, " +
                    "  SUM(CASE WHEN YEAR(STR_TO_DATE(birth_date, '%Y-%m-%d')) <= 1929 THEN 1 ELSE 0 END) AS twenties, " +
                    "  SUM(CASE WHEN YEAR(STR_TO_DATE(birth_date, '%Y-%m-%d')) BETWEEN 1930 AND 1939 THEN 1 ELSE 0 END) AS thirties, " +
                    "  SUM(CASE WHEN YEAR(STR_TO_DATE(birth_date, '%Y-%m-%d')) BETWEEN 1940 AND 1949 THEN 1 ELSE 0 END) AS forties, " +
                    "  SUM(CASE WHEN YEAR(STR_TO_DATE(birth_date, '%Y-%m-%d')) BETWEEN 1950 AND 1959 THEN 1 ELSE 0 END) AS fifties " +
                    "FROM tbl_customer " +
                    "WHERE status = 'ACTIVE'" +
                    "GROUP BY " +
                    "  CASE " +
                    "    WHEN LOCATE('종로구', primary_address) THEN '종로구' " +
                    "    WHEN LOCATE('서대문구', primary_address) THEN '서대문구' " +
                    "    WHEN LOCATE('성북구', primary_address) THEN '성북구' " +
                    "    WHEN LOCATE('용산구', primary_address) THEN '용산구' " +
                    "    ELSE '기타' " +
                    "  END", nativeQuery = true)
    List<SecondGraphData> getSecondGraphData();


    // Third Graph Data
    @Query(value =
            "SELECT " +
                    "    YEAR(regist_date) AS year, " +
                    "    MONTH(regist_date) AS month, " +
                    "    SUM(CASE WHEN gender = 'MALE' THEN 1 ELSE 0 END) AS countMale, " +
                    "    SUM(CASE WHEN gender = 'FEMALE' THEN 1 ELSE 0 END) AS countFemale " +
                    "FROM tbl_customer " +
                    "WHERE " +
                    "    regist_date >= CURDATE() - INTERVAL 4 MONTH " +
                    "GROUP BY " +
                    "    year, " +
                    "    month " +
                    "ORDER BY " +
                    "    year DESC, " +
                    "    month DESC", nativeQuery = true)
    List<ThirdGraphData> getThirdGraphData();

    @Query(nativeQuery = true, value =
            "SELECT subquery.year, allMonths.month, COALESCE(SUM(subquery.totalCount) OVER (ORDER BY subquery.year, allMonths.month ASC), 0) AS cumulativeCount " +
                    "FROM ( " +
                    "    SELECT YEAR(regist_date) AS year, MONTH(regist_date) AS month, COUNT(*) AS totalCount " +
                    "    FROM tbl_customer " +
                    "    GROUP BY YEAR(regist_date), MONTH(regist_date) " +
                    ") AS subquery " +
                    "LEFT JOIN ( " +
                    "    SELECT 1 AS month UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 " +
                    "    UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 " +
                    "    UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 " +
                    ") AS allMonths ON subquery.month = allMonths.month " +
                    "ORDER BY subquery.year DESC, allMonths.month DESC"
    )
    List<FirstGraphData> getFirstGraphData();

}
