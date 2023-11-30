package com.kimleepark.thesilver.common.exception;

import com.kimleepark.thesilver.common.exception.type.ExceptionCode;
import lombok.Getter;

@Getter
public class ServerInternalException extends CustomException{ // CustomException 상속

    public ServerInternalException(final ExceptionCode exceptionCode){
        super(exceptionCode);
    }
}
