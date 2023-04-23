package com.example.lastoauth.security.oauth2;

import com.example.lastoauth.user.Role;
import com.example.lastoauth.user.User;
import com.example.lastoauth.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class GitHubUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {


    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String avatarUrl = oAuth2User.getAttribute("avatar_url");

        if (email == null || email.isEmpty()) {
            email = oAuth2User.getAttribute("url");
        }

        // check if the user already exists in the database
        if (!userService.userExists(email)) {
            // create a new user in the database
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            User userDetails = User.builder()
                    .avatarUrl(avatarUrl)
                    .email(email)
                    .password("")
                    .role(Role.USER)
                    .build();
            userService.createUser(userDetails);
        }

        return oAuth2User;
    }
}

