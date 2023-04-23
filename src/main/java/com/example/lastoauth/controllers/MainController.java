package com.example.lastoauth.controllers;

import com.example.lastoauth.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    private OAuth2AuthorizedClientService authorizedClientService;
    @GetMapping()
    public String index(HttpServletRequest request,Model model){
        OAuth2AuthenticationToken auth = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getPrincipal().getAttribute("name");
        if(name != null || !name.isEmpty()){
            model.addAttribute("name", name);
        }else{
            model.addAttribute("name","Anonimo");
        }



        return "index";
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                                Model model,
                                HttpServletRequest request) {
        if (error != null) {
            AuthenticationException ex = (AuthenticationException) request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                model.addAttribute("errorMessage", ex.getMessage());
            }
        }
        return "login";
    }

    @GetMapping("/getall")
    public String getAll(Model model){
        model.addAttribute("users",userService.getAll());
        return "allusers";
    }
}
