package com.kimleepark.thesilver.employee;

import com.kimleepark.thesilver.employee.type.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name="tbl_employee")
@NoArgsConstructor(access=PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE tbl_employee SET status = 'DELETED' WHERE employee_code = ?")
public class Employee {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long employeeCode;

    @Column(nullable = false)
    private String employeeName;

    @Column(nullable = false)
    private String registrationNumber;

    @Column
    private String workingStatus;

    @Column(nullable = false)
    private String employeeEmail;

    @Column
    private String employeeAddress;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private GenderType gender;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private DisabilityType disability;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private MarriageType marriage;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private PatriotsType patriots;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private EmploymentType employmentType;

    @Column(nullable = false)
    private LocalDateTime joinDate;

    @Column
    private LocalDateTime leaveDate;

    @Column
    private String leaveReason;

    @Column(nullable = false)
    private String employeePhone;

    @Column(nullable = false)
    private LocalDateTime registDate;

    @Column
    private LocalDateTime modifyDate;

    @Column
    private String employeePicture;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rank_code")
    private Rank rank;

    @Enumerated(value = STRING)
    @Column
    private LeaveType leave;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "Employee")
    @JoinColumn(name="team_code")
    private Team team;
}
