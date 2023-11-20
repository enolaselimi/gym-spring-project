package com.gym.controller;

import com.gym.domain.dto.LogInDTO;
import com.gym.domain.dto.LogInRequest;
import com.gym.domain.dto.SignUpDTO;
import com.gym.domain.dto.UserDTO;
import com.gym.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.RoleNotFoundException;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/signup")
    public UserDTO registerUser(@RequestBody @Valid SignUpDTO newUser) throws RoleNotFoundException {
        return authenticationService.registerUser(newUser.getUsername(), newUser.getPassword(), newUser.getAuthority(), newUser.getEntity_id());
    }

    @PostMapping("/login")
    public LogInDTO loginUser(@RequestBody @Valid LogInRequest user){
        return authenticationService.logInUser(user.getUsername(), user.getPassword());
    }
}
