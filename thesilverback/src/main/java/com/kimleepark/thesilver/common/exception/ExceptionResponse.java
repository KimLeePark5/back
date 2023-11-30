package com.kimleepark.thesilver.common.exception;

import com.kimleepark.thesilver.common.exception.type.ExceptionCode;
import lombok.Getter;

@Getter
public class ExceptionResponse {

    private int code;
    private String message;

    public ExceptionResponse(final ExceptionCode exceptionCode){
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }

    public ExceptionResponse(int code, String message){
        this.code = code;
        this.message = message;
    }

    public static ExceptionResponse of(int code, String message) {
        return new ExceptionResponse(code, message);

    }
}
