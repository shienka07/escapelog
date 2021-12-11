package com.recoders.escapelog.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Component
public class UserLoginFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String msg = "";

        if(exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {
            msg = "아이디나 비밀번호가 일치하지 않습니다.";

        }else {
            msg = "알 수 없는 이유로 로그인을 실패하였습니다. 관리자에게 문의하세요.";
        }

        response.sendRedirect("/login?error=true"+"&errorMsg="+ URLEncoder.encode(msg,"UTF-8"));

    }
}
