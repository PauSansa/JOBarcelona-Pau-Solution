package com.example.lastoauth.user;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserToDTO implements Function<User,UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .loginName(user.getLoginName())
                .build();
    }
}
