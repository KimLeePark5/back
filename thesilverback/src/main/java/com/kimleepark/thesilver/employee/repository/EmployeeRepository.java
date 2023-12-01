package com.kimleepark.thesilver.employee.repository;

import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.employee.type.LeaveType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @EntityGraph(attributePaths = {"rank","team"})
    Page<Employee> findByLeaveType(Pageable pageable, LeaveType leaveType);

    Employee findByEmployeeCodeAndLeaveType(Long employeeCode, LeaveType leaveType);

    Optional<Employee> findByEmployeeCode(Long employeeCode);

}
