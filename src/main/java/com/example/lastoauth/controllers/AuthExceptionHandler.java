package com.example.lastoauth.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public String handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, ex);
        return "redirect:/login?error=true";
    }
}

