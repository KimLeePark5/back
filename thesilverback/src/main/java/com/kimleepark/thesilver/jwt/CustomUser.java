package com.kimleepark.thesilver.jwt;

import lombok.Getter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class CustomUser extends User {

    private final Long employeeCode;

    public CustomUser(Long employeeCode, UserDetails userDetails) {
        super(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        this.employeeCode = employeeCode;

    }

    public static CustomUser of(Long employeeCode, UserDetails userDetails) {
        return new CustomUser(
                employeeCode,
                userDetails
        );
    }
}
