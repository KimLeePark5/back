package com.kimleepark.thesilver.common.exception;


import com.kimleepark.thesilver.common.exception.type.ExceptionCode;
import lombok.Getter;

@Getter
public class ServerInternalException extends CustomException {

    public ServerInternalException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
