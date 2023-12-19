package com.kimleepark.thesilver.attend.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kimleepark.thesilver.attend.domain.type.AttendType;
import com.kimleepark.thesilver.attend.dto.request.RequestAttend;
import com.kimleepark.thesilver.common.exception.BadRequestException;
import com.kimleepark.thesilver.common.exception.type.ExceptionCode;
import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.vacation.domain.Require;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.kimleepark.thesilver.attend.domain.type.AttendType.N;
import static com.kimleepark.thesilver.common.exception.type.ExceptionCode.ATTEND_TIME_NULL;

@Entity
@Table(name = "tbl_attend")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Where(clause = "status = 'N'")
public class Attend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int attendNo;


    @JoinColumn(name = "employeeCode")
    @ManyToOne
    private Employee employeeCode;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @CreatedDate
    private LocalDate attendDate;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime entertime;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime leavetime;

    private float attendTime;

    private String type = "";

    private String note = "";

    @Enumerated(EnumType.STRING)
    private AttendType status = N;


    public static final String LATE = "지각";
    public static final String OVERTIME = "연장근무";
    public static final String LEAVE_EARLY = "조퇴";

    public static Attend of() {

        return new Attend();
    }

    public Attend(Employee emp) {
        this.employeeCode = emp;
    }

    public static Attend getEmpNo(Employee emp) {

        return new Attend(emp);
    }

    public static void setEmp(Attend attend, Employee employee) {
        attend.employeeCode = employee;
    }


    public void updateType() {
        this.type = OVERTIME;
    }

    public void setLeaveTime() {
        this.leavetime = LocalTime.now();
    }

    public void setAttendTime(int attendTime) {
        this.attendTime = attendTime;
    }


    public void updateNote(String note) {
        switch (note) {
            case "EARLY_LEAVE":
                this.note = LEAVE_EARLY;
                break;
            case "LATE":
                this.note = LATE;
                break;

        }
    }

    public void updateAttend(RequestAttend requestAttend) {
        if (requestAttend.getEnterTime() != null) {
            this.entertime = requestAttend.getEnterTime();
        }
        if (requestAttend.getLeaveTime() != null) {
            this.leavetime = requestAttend.getLeaveTime();
        }
        if (requestAttend.getType() != null) {
            this.type = requestAttend.getType();
        }
        if (requestAttend.getNote() != null) {
            if (requestAttend.getNote().equals("기본")) {
                this.note = "";
            } else {
                this.note = requestAttend.getNote();
            }
        }


        if (this.entertime != null && this.leavetime == null) {
            throw new BadRequestException(ATTEND_TIME_NULL);
        } else if (this.leavetime != null && this.entertime == null) {
            throw new BadRequestException(ATTEND_TIME_NULL);
        }

        if (requestAttend.getEnterTime() != null || requestAttend.getLeaveTime() != null) {
            Duration diff = Duration.between(this.entertime, this.leavetime);

            if (diff.toHoursPart() >= 4) {
                this.attendTime = diff.toHoursPart() - 1;
            } else {
                this.attendTime = diff.toHoursPart();
            }
        }


    }


    @Override
    public String toString() {
        return "Attend{" +
                "attendNo=" + attendNo +
                ", attendDate=" + attendDate +
                ", entertime=" + entertime +
                ", leavetime=" + leavetime +
                ", attendTime=" + attendTime +
                ", type='" + type + '\'' +
                ", note='" + note + '\'' +
                ", status=" + status +
                '}';
    }

    public void putEnterTime() {
        this.entertime = LocalTime.now();
    }

    public void updateLate() {
        this.note = LATE;
    }

    public void updateLeaveEarly() {
        this.note = LEAVE_EARLY;
    }

    public void updateMorningOff() {
        this.note = "오전반차";
    }

    public void updateafternoonoff() {
        this.note = "오후반차";
    }

    public void updateTypeDefault() {
        this.type = "기본근무";
    }


    public void updateAbsent() {

        this.note = "결근";
    }

}
