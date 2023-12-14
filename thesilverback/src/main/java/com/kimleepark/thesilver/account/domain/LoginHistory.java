package com.kimleepark.thesilver.account.domain;

import com.kimleepark.thesilver.employee.Employee;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.mapping.ToOne;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "tbl_login_history")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class LoginHistory {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long loginCode;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime loginDate;

    @Column(nullable = false)
    private String loginIp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeCode", nullable = false)
    private Employee employee;


    public LoginHistory(Employee employee, String loginIp) {
        this.employee = employee;
        this.loginIp = loginIp;
    }

    public static LoginHistory of(Employee employee, String ip) {
        return new LoginHistory(employee,ip);
    }
}
