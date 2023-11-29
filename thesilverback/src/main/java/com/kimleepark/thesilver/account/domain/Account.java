package com.kimleepark.thesilver.account.domain;

import com.kimleepark.thesilver.account.domain.type.AccountChangeStatus;
import com.kimleepark.thesilver.account.domain.type.AccountStatus;
import com.kimleepark.thesilver.employee.Employee;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.kimleepark.thesilver.account.domain.type.AccountChangeStatus.UNCHANGED;
import static com.kimleepark.thesilver.account.domain.type.AccountStatus.ACTIVE;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "tbl_account")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Account {


    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long accountCode;

    @Column(nullable = false)
    private String employeeNumber;

    @Column(nullable = false)
    private String employeePassword;

    @Column
    private String refreshToken;

    @Column
    private LocalDateTime lastDate;

    @Column(nullable = false)
    private int attemptCount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus status = ACTIVE;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountChangeStatus changeStatus = UNCHANGED;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeCode")
    private Employee employee;

}
