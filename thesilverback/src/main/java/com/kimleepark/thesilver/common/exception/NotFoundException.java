package com.kimleepark.thesilver.common.exception;

import com.kimleepark.thesilver.common.exception.type.ExceptionCode;
import lombok.Getter;

@Getter
public class NotFoundException extends CustomException{

    public NotFoundException(final ExceptionCode exceptionCode){
        super(exceptionCode);
    }
}
