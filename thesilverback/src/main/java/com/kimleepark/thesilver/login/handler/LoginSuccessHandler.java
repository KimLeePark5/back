package com.kimleepark.thesilver.login.handler;

import com.kimleepark.thesilver.account.domain.Account;
import com.kimleepark.thesilver.account.domain.type.AccountStatus;
import com.kimleepark.thesilver.jwt.CustomUser;
import com.kimleepark.thesilver.jwt.service.JwtService;
import com.kimleepark.thesilver.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.RequestUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.stream.Collectors;

/* 로그인 성공 핸들러 */
@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final LoginService loginService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {


        /* 로그인 성공 후 저장 된 인증 객체에서 정보를 꺼낸다. */
        Map<String, String> employeeInfo = getEmployeeInfo(authentication);
        log.info("authentication : {}", authentication.getPrincipal());
        log.info("authentication : {}", authentication.getCredentials());
        log.info("authentication : {}", authentication.getAuthorities());
        log.info("로그인 성공 후 인증 객체에서 꺼낸 정보 : {}", employeeInfo);

        // 로그인 시도 횟수 초기화
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        loginService.resetAttemptCount(userDetails.getUsername());

        /* access token과 refresh token 생성 */
        String accessToken = jwtService.createAccessToken(employeeInfo);
        String refreshToken = jwtService.createRefreshToken();

        log.info("발급 된 accessToken : {}", accessToken);
        log.info("발급 된 refreshToken : {}", refreshToken);

        /* 응답 헤더에 발급 된 토큰을 담는다. */
        response.setHeader("Access-Token", accessToken);
        response.setHeader("Refresh-Token", refreshToken);

        /* 응답 헤더에 계정의 status를 담는다. */
        AccountStatus accountStatus = loginService.getAccountStatus(userDetails.getUsername());
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.write("{ \"accountStatus\": \"" + accountStatus.toString() + "\" }");
        writer.flush();

        /* 로그인 기록을 남긴다. */
        // 프록시 서버나 로드 밸런서를 사용할 경우
        //String ip = request.getHeader("X-Forwarded-For");

        // 로컬 환경에서 임시로 아이피 발급
        String ip = request.getRemoteAddr();
        // 사번 찾아오기
        String employeeNumber = userDetails.getUsername();
        loginService.saveLoginHistory(employeeNumber,ip);

        response.setStatus(HttpServletResponse.SC_OK);

        /* 발급한 refresh token을 DB에 저장해 둔다. */
        jwtService.updateRefreshToken(employeeInfo.get("employeeNumber"), refreshToken);

    }

    private Map<String, String> getEmployeeInfo(Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String employeeRole = userDetails.getAuthorities()
                .stream().map(auth -> auth.getAuthority().toString())
                .collect(Collectors.joining());

        return Map.of(
                "employeeNumber", userDetails.getUsername(),
                "employeeRole", employeeRole
        );
    }
}
