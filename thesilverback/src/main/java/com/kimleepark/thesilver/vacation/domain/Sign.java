package com.kimleepark.thesilver.vacation.domain;

import com.kimleepark.thesilver.employee.Employee;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_sign")
@NoArgsConstructor(access = PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Sign {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long SignNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reqNo")
    private Require require;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeCode")
    private Employee employee; // 결재자

    @Column
    private String signStatus;

    @Column
    private String returnCause;

    @Column
    private String cancelCause;

    @Column
    private LocalDate approveDate;

    @Column
    private LocalDateTime returnDate;

    @Column
    private LocalDateTime cancelDate;

    public Sign(Require savedRequire, Employee employee) {
        this.require = savedRequire;
        this.employee = employee;
    }

    public static Sign of(Require savedRequire, Employee employee) {
        return new Sign(savedRequire, employee);
    }
}


