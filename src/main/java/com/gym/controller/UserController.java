package com.gym.controller;

import com.gym.domain.dto.SignUpDTO;
import com.gym.domain.dto.UserDTO;
import com.gym.domain.entity.Client;
import com.gym.domain.entity.Instructor;
import com.gym.domain.entity.Role;
import com.gym.domain.entity.User;
import com.gym.domain.exception.ResourceNotFoundException;
import com.gym.domain.mapper.ClientConverter;
import com.gym.domain.mapper.InstructorConverter;
import com.gym.domain.mapper.UserConverter;
import com.gym.repository.RoleRepository;
import com.gym.repository.UserRepository;
import com.gym.service.ClientService;
import com.gym.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientService clientService;
    @Autowired
    private InstructorService instructorService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDTO>> findAllUsers() {
        List<UserDTO> users = userRepository.findAll().stream().map(UserConverter::toDTO).toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> findUserById(@PathVariable Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new ResourceNotFoundException("User not found");
        }
        User userEntity = user.get();
        UserDTO userDTO = UserConverter.toDTO(userEntity);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateUser(@PathVariable Integer userId, @RequestBody SignUpDTO updatedUser) {
        Optional<User> user =  userRepository.findById(userId);
        if(user.isEmpty()){
            throw new ResourceNotFoundException("User not found");
        }
        User userEntity = user.get();
        Set< Role > authorities = new HashSet<>();
        String authority = updatedUser.getAuthority();
        Optional<Role> role = roleRepository.findByAuthority(authority);
        Role roleEntity = role.isPresent() ? role.get() : null;
        authorities.add(roleEntity);
        if(authority.equalsIgnoreCase("instructor")){
            Instructor instructor = InstructorConverter.fromDTOtoEntity(instructorService.findById(updatedUser.getEntity_id()));
            userEntity.setInstructor(instructor);
        } else if (authority.equalsIgnoreCase("client")) {
            Client client = ClientConverter.fromDTOtoEntity(clientService.findById(updatedUser.getEntity_id()));
            userEntity.setClient(client);
        }
        userEntity.setUsername(updatedUser.getUsername());
        userEntity.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        userEntity.setAuthorities(authorities);

        userRepository.save(userEntity);
        return ResponseEntity.ok("User updated successfully");

    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
