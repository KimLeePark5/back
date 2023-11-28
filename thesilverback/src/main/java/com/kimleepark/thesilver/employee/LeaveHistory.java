package com.kimleepark.thesilver.employee;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name="tbl_leave_history")
public class LeaveHistory {

    @Id
    private Long leaveCode;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column
    private LocalDateTime backDate;

    @Column(nullable = false)
    private Long employeeCode;
}
