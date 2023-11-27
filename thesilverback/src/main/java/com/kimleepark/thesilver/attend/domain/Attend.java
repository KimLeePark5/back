package com.kimleepark.thesilver.attend.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kimleepark.thesilver.attend.domain.type.AttendType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
@Table(name = "tbl_attend")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Where(clause = "status = 'N'")
@ToString
public class Attend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int attendNo;

    @Column(nullable = false)
    private int employeeCode;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @CreatedDate
    private LocalDate attendDate;

    @JsonFormat(pattern = "HH:mm:ss")
    @CreatedDate
    private LocalTime entertime;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime leavetime;

    private int attendTime;

    private String type;

    private String note = "null";

    @Enumerated(EnumType.STRING)
    private AttendType status = AttendType.N;

    public static Attend of() {

        return new Attend();
    }

    public static void setEmp(Attend attend,int empNo){
        attend.employeeCode = empNo;
    }
    public static void setLeaveTime(Attend attend){
        attend.leavetime = LocalTime.now();
    }
}
