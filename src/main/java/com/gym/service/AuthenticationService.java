package com.gym.service;


import com.gym.domain.dto.LogInDTO;
import com.gym.domain.dto.UserDTO;
import com.gym.domain.entity.Role;
import com.gym.domain.entity.User;
import com.gym.domain.exception.LogInFailedException;
import com.gym.domain.mapper.UserConverter;
import com.gym.repository.RoleRepository;
import com.gym.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    public UserDTO registerUser(String username, String password, String role){
        String passwordEncoded = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority(role.toUpperCase()).get();
        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);
        User user = new User(0, username, passwordEncoded, authorities);
        return UserConverter.toDTO(userRepository.save(user));
    }


    public LogInDTO logInUser(String username, String password){
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = tokenService.generateToken(auth);
            return new LogInDTO(userRepository.findUserByUsername(username).get(), token);
        }catch(AuthenticationException e){
            throw new LogInFailedException("Incorrect log in info.");
        }
    }
}
