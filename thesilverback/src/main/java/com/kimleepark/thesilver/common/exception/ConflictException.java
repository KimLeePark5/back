package com.kimleepark.thesilver.common.exception;


import com.kimleepark.thesilver.common.exception.type.ExceptionCode;
import lombok.Getter;

@Getter
public class ConflictException extends CustomException {

    public ConflictException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
