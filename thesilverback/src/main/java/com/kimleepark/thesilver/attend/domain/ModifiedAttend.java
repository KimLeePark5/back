package com.kimleepark.thesilver.attend.domain;

import com.kimleepark.thesilver.attend.dto.request.RequestAttend;
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
public class ModifiedAttend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long modifiedNo;

    private int attendNo;

    private int employeeCode;
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;
    private LocalTime beforeEntertime;
    private LocalTime afterEntertime;
    private LocalTime beforeLeavetime;
    private LocalTime afterLeavetime;
    private String note;
    private String type;

    public ModifiedAttend(Attend attend, int empNo, LocalTime beforeEntertime, LocalTime afterEnterTime, LocalTime beforLeavetime, LocalTime AfterLeaveTime, String note, String type) {
        this.attendNo = attend.getAttendNo();
        this.employeeCode = empNo;
        this.beforeEntertime = beforeEntertime;
        this.afterEntertime = afterEnterTime;
        this.beforeLeavetime = beforLeavetime;
        this.afterLeavetime = AfterLeaveTime;
        this.note = note;
        this.type = type;
    }

    public static ModifiedAttend of(Attend attend, RequestAttend requestAttend, int empNo) {
        return new ModifiedAttend(
            attend,
            empNo,
            attend.getEntertime(),
            requestAttend.getEnterTime(),
            attend.getLeavetime(),
            requestAttend.getLeaveTime(),
                requestAttend.getNote(),
                requestAttend.getType()
        );
    }
}
