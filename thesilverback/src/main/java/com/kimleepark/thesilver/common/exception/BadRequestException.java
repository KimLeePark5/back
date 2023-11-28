package com.kimleepark.thesilver.common.exception;


import com.kimleepark.thesilver.common.exception.type.ExceptionCode;
import lombok.Getter;

@Getter
public class BadRequestException extends CustomException {
    public BadRequestException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
