package com.kimleepark.thesilver.jwt.filter;

import com.kimleepark.thesilver.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/* Request에서 Token을 꺼내서 인증 확인하는 필터
* (로그인 외에 인증이 필요한 요청들을 처리)
* */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        /* 로그인 요청의 경우 다음 필터(로그인 필터)로 진행 */
        if(request.getRequestURI().equals("/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        if(request.getRequestURI().equals("/api/v1/password-reset")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getRequestURI().startsWith("/programimgs/")) {
            filterChain.doFilter(request, response);
            return;
        }


        /* 1. 사용자 헤더에서 Refresh Token 추출 */
        String refreshToken = jwtService.getRefreshToken(request)
                .filter(jwtService::isValidToken)
                .orElse(null);

        log.info("refreshToken : {}", refreshToken);

        /* 2-1. Refresh Token이 있다면?
        * - AccessToken 만료 상황
        * - DB에서 Refresh Token 일치 여부 확인 후 일치하면 AccessToken 재발급
        * */
        if(refreshToken != null) {
            jwtService.checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return;
        }

        /* 2-2. Refresh Token이 없다면?
        * - AccessToken 유효성 확인
        * */
        if(refreshToken == null) {
            log.info("확인");
            jwtService.checkAccessTokenAndAuthentication(request, response, filterChain);
            log.info("확인2");
        }

    }
}













