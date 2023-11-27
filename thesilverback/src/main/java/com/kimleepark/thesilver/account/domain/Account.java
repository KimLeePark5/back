package com.kimleepark.thesilver.account.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_account")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Account {


    @Id
    private Long employeeCode;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "employeeCode")
//    private Employee employee;

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
    private String status;

    @Column(nullable = false)
    private String changeStatus;

}
