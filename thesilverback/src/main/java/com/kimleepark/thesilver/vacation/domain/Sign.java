package com.kimleepark.thesilver.vacation.domain;

import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.vacation.domain.type.RequireStatusType;
import com.kimleepark.thesilver.vacation.domain.type.SignStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_sign")
@NoArgsConstructor
@Getter
@Setter
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
    @Enumerated(value = EnumType.STRING)
    private SignStatusType signStatus;

    @Column
    private String returnCause;

    @Column
    private String cancelCause;

    @Column

    private LocalDateTime approveDate;

    @Column

    private LocalDateTime returnDate;

    @Column

    private LocalDateTime cancelDate;

    public Sign(Require savedRequire, Employee employee, SignStatusType signStatus) {
        this.require = savedRequire;
        this.employee = employee;
        this.signStatus = signStatus;
    }

    public static Sign of(Require savedRequire, Employee employee, SignStatusType signStatus) {
        return new Sign(savedRequire, employee, SignStatusType.PROCEED);
    }

    public void update(String signStatus, String returnCause, String cancelCause,
                       LocalDateTime approveDate, LocalDateTime returnDate, LocalDateTime cancelDate) {
        this.signStatus = SignStatusType.valueOf(signStatus);
        this.returnCause = returnCause;
        this.cancelCause = cancelCause;
        this.approveDate = approveDate;
        this.returnDate = returnDate;
        this.cancelDate = cancelDate;
    }

    /* 결재 상태에 따른 시간 기록 */
    public void updatePass() {
        this.signStatus = SignStatusType.PASS;
        this.approveDate = LocalDateTime.now();
    }

    public void updateReturn(String returnCause) {
        this.signStatus = SignStatusType.RETURN;
        this.returnCause = returnCause;
        this.returnDate = LocalDateTime.now();
    }

    public void updateCancel(String cancelCause) {
        this.signStatus = SignStatusType.CANCEL;
        this.cancelCause = cancelCause;
        this.cancelDate = LocalDateTime.now();
    }

}


