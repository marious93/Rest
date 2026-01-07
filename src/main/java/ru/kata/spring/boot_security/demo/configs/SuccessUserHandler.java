package ru.kata.spring.boot_security.demo.configs;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.Set;

import static java.util.Arrays.stream;

@Component
public class SuccessUserHandler implements AuthenticationSuccessHandler {
    private static final String USER = "USER";
    private static final String ADMIN = "ADMIN";


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.stream().anyMatch(s -> s.contains(ADMIN))) {
            response.sendRedirect("/test.html");
        } else if (roles.stream().anyMatch(s -> s.contains(USER))) {
            response.sendRedirect("/user");
        } else {
            response.sendRedirect("/");
        }
    }
}