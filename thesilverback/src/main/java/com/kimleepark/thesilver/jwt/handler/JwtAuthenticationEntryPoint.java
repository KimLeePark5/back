package com.kimleepark.thesilver.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kimleepark.thesilver.common.exception.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.kimleepark.thesilver.common.exception.type.ExceptionCode.UNAUTHORIZED;


/* 인증 실패 처리 */
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    /* 유효한 자격 증명(token)을 제공하지 않고 접근하려 하는 경우 인증 실패이므로 401 오류 반환 */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(new ExceptionResponse(UNAUTHORIZED)));
    }
}












