package com.kimleepark.thesilver.employee.repository;

import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.employee.LeaveHistory;
import com.kimleepark.thesilver.employee.dto.CustomerEmployeeResponse;
import com.kimleepark.thesilver.employee.type.LeaveType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @EntityGraph(attributePaths = {"rank","team","leaveHistoryList"})
    Page<Employee> findByLeave(Pageable pageable, LeaveType leaveType);

    Employee findByEmployeeCodeAndLeave(Long employeeCode, LeaveType leaveType);


}
