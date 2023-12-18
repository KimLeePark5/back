package com.kimleepark.thesilver.attend.domain;

import com.kimleepark.thesilver.attend.dto.request.RequestAttend;
import com.kimleepark.thesilver.employee.Employee;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "tbl_modifiedattend")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@ToString
@Getter
public class ModifiedAttend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long modifiedNo;
    private int attendNo;
    @JoinColumn(name = "employee_code")
    @ManyToOne
    private Employee employeeCode;
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;
    private LocalTime beforeEntertime;
    private LocalTime afterEntertime;
    private LocalTime beforeLeavetime;
    private LocalTime afterLeavetime;
    private String beforeNote;
    private String afterNote;
    private String beforeType;
    private String afterType;

    public ModifiedAttend(Attend attend, Employee empNo, LocalTime beforeEntertime, LocalTime afterEnterTime,
                          LocalTime beforLeavetime, LocalTime AfterLeaveTime, String beforeNote,String afterNote,
                          String beforeType, String afterType) {
        this.attendNo = attend.getAttendNo();
        this.employeeCode = empNo;
        this.beforeEntertime = beforeEntertime;
        this.afterEntertime = afterEnterTime;
        this.beforeLeavetime = beforLeavetime;
        this.afterLeavetime = AfterLeaveTime;
        this.beforeNote = beforeNote;
        this.afterNote = afterNote;
        this.beforeType = beforeType;
        this.afterType = afterType;
    }

    public static ModifiedAttend of(Attend attend, RequestAttend requestAttend, Employee empNo) {
        return new ModifiedAttend(
                attend,
                empNo,
                attend.getEntertime(),
                requestAttend.getEnterTime(),
                attend.getLeavetime(),
                requestAttend.getLeaveTime(),
                attend.getNote(),
                requestAttend.getNote(),
                attend.getType(),
                requestAttend.getType()
        );
    }
}
