package com.kimleepark.thesilver.jwt;

import lombok.Getter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class CustomUser extends User {

    private final Long employeeCode;
    private final String employeeName;

    public CustomUser(Long employeeCode, String employeeName, UserDetails userDetails) {
        super(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
    }

    public static CustomUser of(Long employeeCode,String employeeName, UserDetails userDetails) {
        return new CustomUser(
                employeeCode,
                employeeName,
                userDetails
        );
    }
}
