package com.kimleepark.thesilver.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kimleepark.thesilver.common.exception.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.kimleepark.thesilver.common.exception.type.ExceptionCode.FAIL_LOGIN;
import static com.kimleepark.thesilver.common.exception.type.ExceptionCode.NOT_FOUND_EMPLOYEE_NUMBER;

/* 로그인 실패 처리 핸들러 */
@Slf4j
@RequiredArgsConstructor
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("onAuthenticationFailure 확인 : {}", exception.toString());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String errorMessage;

        if (exception.getMessage().equals("사번에 해당하는 직원이 없습니다.")) {
            response.getWriter().write(objectMapper.writeValueAsString(new ExceptionResponse(NOT_FOUND_EMPLOYEE_NUMBER)));
        } else {
            response.getWriter().write(objectMapper.writeValueAsString(new ExceptionResponse(FAIL_LOGIN)));
        }
    }
}
