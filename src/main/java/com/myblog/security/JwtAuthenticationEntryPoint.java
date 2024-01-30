package com.myblog.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
         response.sendError(HttpServletResponse.SC_UNAUTHORIZED,authException.getMessage());
    }
}
//This  JwtAuthenticationEntryPoint class is for the exceptions ,
//for example from the url when you are sending a token if that  token is not valid then it throws the exception