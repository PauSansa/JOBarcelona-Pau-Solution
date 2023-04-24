package com.example.lastoauth.controllers;

import com.example.lastoauth.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
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
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/star")
    public String starRepository() {


        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if(authToken == null){
            return "redirect:/";
        }
        String clientId = authToken.getAuthorizedClientRegistrationId();
        String principalName = authToken.getName();

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(clientId, principalName);

        OAuth2AccessToken accessToken = client.getAccessToken();
        String tokenValue = accessToken.getTokenValue();







        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokenValue);
        headers.set("Content-Length", "0");
        headers.set("Accept", "application/vnd.github+json");
        headers.set("X-GitHub-Api-Version", "2022-11-28");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://api.github.com/user/starred/PauSansa/JOBarcelona-Pau-Solution";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);


        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            System.out.println("Starred repository ");
        } else {
            System.out.println("Error starring repository ");
        }


        return "redirect:/?starred=true";
    }


    @GetMapping()
    public String index(HttpServletRequest request,Model model, @RequestParam(required = false) String starred){
        OAuth2AuthenticationToken auth = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        if(starred != null){
            model.addAttribute("starred","You have been starred this repo succesfully!");
        }




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
