package com.kimleepark.thesilver.employee.repository;

import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.employee.Team;
import com.kimleepark.thesilver.employee.type.GenderType;
import com.kimleepark.thesilver.employee.type.LeaveType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.GenerationType;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @EntityGraph(attributePaths = {"rank","team"})
    Page<Employee> findByLeaveType(Pageable pageable, LeaveType leaveType);

    @EntityGraph(attributePaths = {"rank","team"})
    Page<Employee> findByEmployeeCodeLikeAndLeaveType(Pageable pageable, Long code, LeaveType leaveType);
    @EntityGraph(attributePaths = {"rank","team"})
    Page<Employee> findByRankRankNameContainingAndLeaveType(Pageable pageable, String rankName, LeaveType leaveType);
    @EntityGraph(attributePaths = {"rank","team"})
    Page<Employee> findByEmployeeNameContainingAndLeaveType(Pageable pageable, String searchValue, LeaveType leaveType);
    @EntityGraph(attributePaths = {"rank","team"})
    Page<Employee> findByGenderLikeAndLeaveType(Pageable pageable, GenderType gender, LeaveType leaveType);
    @EntityGraph(attributePaths = {"rank","team"})
    Page<Employee> findByJoinDateAndLeaveType(Pageable pageable, String JoinDate, LeaveType leaveType);

    Employee findByEmployeeCodeAndLeaveType(Long employeeCode, LeaveType leaveType);

    Optional<Employee> findByEmployeeCode(Long employeeCode);

    Optional<Object> findByEmployeeName(String employeeName);

    Optional<Employee> findByEmployeeEmail(String employeeEmail);
    @EntityGraph(attributePaths = {"rank","team"})
    Page<Employee> findByEmployeeNameContaining(Pageable pageable, String name);

    Page<Employee> findAllByOrderByTeam(Pageable pageable);


    @Query("SELECT DISTINCT e.employeeName FROM Employee e")
    List<String> findAllEmployeeName();

    Employee findByTeamAndRankRankCode(Team team, long l);

    Employee findByRankRankCode(long l);
}