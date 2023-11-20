package com.gym.service;

import com.gym.domain.dto.LogInDTO;
import com.gym.domain.dto.UserDTO;
import com.gym.domain.entity.Client;
import com.gym.domain.entity.Instructor;
import com.gym.domain.entity.Role;
import com.gym.domain.entity.User;
import com.gym.domain.exception.LogInFailedException;
import com.gym.domain.mapper.ClientConverter;
import com.gym.domain.mapper.InstructorConverter;
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

import javax.management.relation.RoleNotFoundException;
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

    @Autowired
    private ClientService clientService;

    @Autowired
    private InstructorService instructorService;

    public UserDTO registerUser(String username, String password, String role, Integer entity_id) throws RoleNotFoundException {
        String passwordEncoded = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority(role.toUpperCase()).get();
        if (userRole == null) {
            throw new RoleNotFoundException("Role not found: " + role);
        }
        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);
        User user = new User(0, username, passwordEncoded, authorities, null,null);
        if(role.equalsIgnoreCase("client")){
            Client client = ClientConverter.fromDTOtoEntity(clientService.findById(entity_id));
            user.setClient(client);
        }
        if(role.equalsIgnoreCase("instructor")){
            Instructor instructor = InstructorConverter.fromDTOtoEntity(instructorService.findById(entity_id));
            user.setInstructor(instructor);
        }

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
