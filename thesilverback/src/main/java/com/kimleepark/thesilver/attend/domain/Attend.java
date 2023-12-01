package com.kimleepark.thesilver.attend.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kimleepark.thesilver.attend.domain.type.AttendType;
import com.kimleepark.thesilver.attend.dto.request.RequestAttend;
import com.kimleepark.thesilver.employee.Employee;
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
    @CreatedDate
    private LocalTime entertime;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime leavetime;

    private float attendTime;

    private String type;

    private String note = "null";

    @Enumerated(EnumType.STRING)
    private AttendType status = AttendType.N;

    public static final String LATE = "지각";
    public static final String OVERTIME = "연장근무";
    public static final String LEAVE_EARLY = "조퇴";
    public static Attend of() {

        return new Attend();
    }

    public static void setEmp(Attend attend, Employee employee){
        attend.employeeCode = employee;
    }


    public void updateType() {
        this.type = OVERTIME;
    }
    public void setLeaveTime(){
        this.leavetime = LocalTime.now();
    }

    public void setAttendTime(float attendTime) {
        this.attendTime = attendTime;
    }

    public void updateNote(String note) {
        switch(note){
            case "EARLY_LEAVE" : this.updateNote(LEAVE_EARLY);
            break;
            case "LATE" : this.updateNote(LATE);
            break;

        }
    }

    public void updateAttend(RequestAttend requestAttend) {
        if (requestAttend.getEnterTime() != null){
            this.entertime = requestAttend.getEnterTime();
        }
        if(requestAttend.getLeaveTime() != null){
            this.leavetime = requestAttend.getLeaveTime();
        }
        if(requestAttend.getType() != null){
            this.type = requestAttend.getType();
        }
        if(requestAttend.getNote() != null){
            this.note = requestAttend.getNote();
        }

        Duration diff = Duration.between(this.entertime,this.leavetime);

        this.attendTime = (float) diff.toMinutes() /60;

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
}
