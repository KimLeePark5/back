package com.kimleepark.thesilver.vacation.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.vacation.domain.type.RequireStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "tbl_require")
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Require {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long reqNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacationTypeCode")
    private VacationType vacationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeCode")
    private Employee employee;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startDate;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime endDate;

    @Column(nullable = false)
    private String reqContent;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RequireStatusType reqStatus;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime reqDate;


    public Require(VacationType vacationType, Employee employee, LocalDateTime startDate, LocalDateTime endDate, String reqContent, RequireStatusType reqStatus) {
        this.vacationType = vacationType;
        this.employee = employee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reqContent = reqContent;
        this.reqStatus = reqStatus;
    }

    public static Require of(VacationType vacationType, Employee employee, LocalDateTime startDate, LocalDateTime endDate, String reqContent, RequireStatusType reqStatus) {
        return new Require(
                vacationType,
                employee,
                startDate,
                endDate,
                reqContent,
                reqStatus);
    }
}
