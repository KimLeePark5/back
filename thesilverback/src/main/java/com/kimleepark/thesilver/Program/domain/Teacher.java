package com.kimleepark.thesilver.Program.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "tbl_teacher")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code; //프로그램 번호

    @Column(nullable = false)
    private String name; // 강사 이름

    @Column(nullable = false)
    private String birthDate; // 생년월일

    @Column(nullable = false)
    private String gender; // 강사 성별

    @Column(nullable = false)
    private String phone; // 강사 연락처

    @Column(nullable = false)
    private String postNo; // 우편번호

    @Column(nullable = false)
    private String address; // 주소

    @Column(nullable = false)
    private String detailAddress; // 강사 상세 주소

    @Column(nullable = false)
    private String profilePicture; // 강사 프로필 사진


//    public Teacher(Long code, String name, String birthDate, String gender, String phone, String postNo, String address, String detailAddress, String profilePicture) {
//        this.code = code;
//        this.name = name;
//        this.birthDate = birthDate;
//        this.gender = gender;
//        this.phone = phone;
//        this.postNo = postNo;
//        this.address = address;
//        this.detailAddress = detailAddress;
//        this.profilePicture = profilePicture;
//    }


}
