package com.kimleepark.thesilver.employee;

import com.kimleepark.thesilver.attend.domain.Attend;
import com.kimleepark.thesilver.employee.type.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name="tbl_employee")
@NoArgsConstructor(access=PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE tbl_employee SET leave_type = 'YES' WHERE employee_code = ?")
public class Employee {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long employeeCode;

    @Column(nullable = false)
    private String employeeName;

    @Column(nullable = false)
    private String registrationNumber;

    @Enumerated(value = STRING)
    @Column
    private WorkingType workingStatus;

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

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime registDate;

    @LastModifiedDate
    @Column
    private LocalDateTime modifyDate;

    @Column
    private String employeePicture;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="rank_code")
    private Rank rank;

    @Enumerated(value = STRING)
    @Column
    private LeaveType leaveType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="team_code")
    private Team team;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="employeeCode")
    @Fetch(FetchMode.SUBSELECT)
    private List<LeaveHistory> leaveHistoryList;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="employeeCode")
    @Fetch(FetchMode.SUBSELECT)
    private List<RankHistory> rankHistoryList;

    @OneToMany(mappedBy = "employeeCode")
    private List<Attend> attendList;

    public Employee(String employeePicture, Long rankCode, String employeeName, String employeeEmail, GenderType gender, DisabilityType disability, MarriageType marriage, PatriotsType patriots, EmploymentType employmentType, WorkingType workingStatus, LeaveType leaveType, String registrationNumber, String employeePhone, String employeeAddress, LocalDateTime joinDate, Team team, Rank rank, String s) {
        this.employeeName = employeeName;
        this.registrationNumber = registrationNumber;
        this.workingStatus = workingStatus;
        this.employeeEmail = employeeEmail;
        this.employeeAddress = employeeAddress;
        this.gender = gender;
        this.disability = disability;
        this.marriage = marriage;
        this.patriots = patriots;
        this.employmentType = employmentType;
        this.joinDate = joinDate;
        this.employeePhone = employeePhone;
        this.employeePicture = employeePicture;
        this.rank = rank;
        this.leaveType = leaveType;
        this.team = team;
    }


    public void updateEmployeePicture(String employeePicture) {
        this.employeePicture = employeePicture;
    }

    public void updates(String employeePicture, Rank rank, String employeeName, String employeeEmail, GenderType gender, DisabilityType disability, MarriageType marriage, PatriotsType patriots, EmploymentType employmentType, WorkingType workingStatus, LeaveType leaveType, String registrationNumber, String employeePhone, String employeeAddress, LocalDateTime joinDate, LocalDateTime leaveDate, String leaveReason, Team team) {
        this.employeePicture = employeePicture;
        this.rank = rank;
        this.employeeName = employeeName;
        this.employeeEmail = employeeEmail;
        this.gender = gender;
        this.disability = disability;
        this.marriage = marriage;
        this.patriots = patriots;
        this.employmentType = employmentType;
        this.workingStatus = workingStatus;
        this.leaveType = leaveType;
        this.registrationNumber = registrationNumber;
        this.employeePhone = employeePhone;
        this.employeeAddress = employeeAddress;
        this.joinDate = joinDate;
        this.leaveDate = leaveDate;
        this.leaveReason = leaveReason;
        this.team = team;
    }

    public void update(String employeeEmail, String employeePhone, String employeeAddress) {
        this.employeeEmail = employeeEmail;
        this.employeePhone = employeePhone;
        this.employeeAddress = employeeAddress;
    }

    public static Employee of(
            final String employeePicture, final Long rankCode, final String employeeName, final String employeeEmail, final GenderType gender, final DisabilityType disability, final MarriageType marriage, final PatriotsType patriots, final EmploymentType employmentType, final WorkingType workingStatus, final LeaveType leaveType, final String registrationNumber, final String employeePhone, String employeeAddress, final LocalDateTime joinDate, final Team team, final Rank rank, final String s) {
        return new Employee(
                employeePicture,
                rankCode,
                employeeName,
                employeeEmail,
                gender,
                disability,
                marriage,
                patriots,
                employmentType,
                workingStatus,
                leaveType,
                registrationNumber,
                employeePhone,
                employeeAddress,
                joinDate,
                team,
                rank,
                s
        );
    }


}
