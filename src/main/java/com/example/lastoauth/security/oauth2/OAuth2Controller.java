package com.example.lastoauth.security.oauth2;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login/oauth2/code/github")
public class OAuth2Controller {

    private OAuth2AuthorizedClientService authorizedClientService;

    public OAuth2Controller(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping
    public String loginSuccess(@AuthenticationPrincipal OAuth2User oauth2User, OAuth2AuthenticationToken authentication) {
        String clientRegistrationId = authentication.getAuthorizedClientRegistrationId();
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(clientRegistrationId, authentication.getName());

        // Aquí puedes obtener los detalles del usuario que se autenticó en GitHub
        String githubLogin = oauth2User.getAttribute("login");
        String githubName = oauth2User.getAttribute("name");
        String githubEmail = oauth2User.getAttribute("email");

        // Aquí puedes guardar el usuario en tu base de datos
        // ...
        System.out.println(githubLogin);

        return "redirect:/alfin";
    }
}

