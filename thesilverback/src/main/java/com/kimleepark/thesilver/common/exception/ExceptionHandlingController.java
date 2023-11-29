package com.kimleepark.thesilver.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlingController {

    /* 400 : Bad Request */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> badRequestException(BadRequestException e) {

        final ExceptionResponse exceptionResponse
                = ExceptionResponse.of(e.getCode(), e.getMessage());

        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    /* 401 : UnAuthorized 인증 오류 => 이미 처리 되어 있음 */

    /* 403 : Forbidden 인가 오류 => 이미 처리 되어 있음 */

    /* 404 : Not Found */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> notFoundException(NotFoundException e) {

        final ExceptionResponse exceptionResponse
                = ExceptionResponse.of(e.getCode(), e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    /* 409 : Conflict => 충돌. 논리적으로 해당 기능을 수행할 수 없는 경우 처리 */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ExceptionResponse> conflictException(ConflictException e) {

        final ExceptionResponse exceptionResponse
                = ExceptionResponse.of(e.getCode(), e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponse);
    }

    /* 500 : Server Errors */
    @ExceptionHandler(ServerInternalException.class)
    public ResponseEntity<ExceptionResponse> serverInternalException(ServerInternalException e) {

        final ExceptionResponse exceptionResponse
                = ExceptionResponse.of(e.getCode(), e.getMessage());

        return ResponseEntity.internalServerError().body(exceptionResponse);
    }

    /* Validation Exception */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> methodValidException(MethodArgumentNotValidException e) {

        String defaultMessage = e.getBindingResult().getFieldError().getDefaultMessage();

        final ExceptionResponse exceptionResponse
                = ExceptionResponse.of(9000, defaultMessage);

        return ResponseEntity.badRequest().body(exceptionResponse);
    }















}
