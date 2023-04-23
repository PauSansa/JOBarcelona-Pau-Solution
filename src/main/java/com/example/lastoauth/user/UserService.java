package com.example.lastoauth.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserToDTO userToDTO;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow();
    }

    public boolean userExists(String username){
        return userRepository.existsByEmail(username);
    }

    public User createUser(User user){
        return userRepository.save(user);
    }

    public List<UserDTO> getAll(){
        return userRepository.findAll().stream().map(userToDTO).collect(Collectors.toList());
    }
}
