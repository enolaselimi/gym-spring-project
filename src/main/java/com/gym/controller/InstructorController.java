package com.gym.controller;

import com.gym.aspect.TrackExecutionTime;
import com.gym.domain.dto.*;
import com.gym.domain.entity.Instructor;
import com.gym.domain.entity.User;
import com.gym.domain.exception.ResourceNotFoundException;
import com.gym.domain.exception.SortingException;
import com.gym.domain.filter.Filter;
import com.gym.domain.mapper.InstructorConverter;
import com.gym.service.InstructorService;
import com.gym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/instructors")
public class InstructorController {
    @Autowired
    private InstructorService instructorService;
    @Autowired
    private UserService userService;

    @GetMapping
    @TrackExecutionTime
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CLIENT')")
    public ResponseEntity<List<InstructorDTO>> findAll(@RequestParam(required = false) String name,
                                                       @RequestParam(required = false) String sort,
                                                       @RequestParam(required = false) Integer pageNumber,
                                                       @RequestParam(required = false) Integer pageSize){
        if(name != null){
            name = name.replace("%20"," ");
        }
        Filter nameFilter = new Filter("name", name, "LIKE", null,pageNumber,pageSize);

        if(sort != null){
            String[] sortValue = sort.split(":");
            if(Objects.equals(sortValue[0], "name")){
                nameFilter.setSort(sortValue[1]);
            } else{
                throw new RuntimeException("Invalid sort field.");
            }
        }

        List<InstructorDTO> instructors = instructorService.findAll(nameFilter);
        if(instructors == null){
            throw new ResourceNotFoundException("Instructors not found.");
        }
        return ResponseEntity.ok(instructors);
    }

    @GetMapping("/{instructorId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    public ResponseEntity<InstructorDTO> findById(@PathVariable Integer instructorId, Authentication auth){
        if(isAuthorized(auth, instructorId)){
            InstructorDTO instructorDTO = instructorService.findById(instructorId);
            if(instructorDTO == null){
                throw new ResourceNotFoundException("Exercise not found.");
            }
            return ResponseEntity.ok(instructorDTO);
        }else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> createInstructor(@RequestBody InstructorRequest instructorRequest,
                                               UriComponentsBuilder ucb){
        var createdEntity = instructorService.save(instructorRequest);
        URI locationOfCreatedEntity = ucb
                .path("api/instructors/{instructorId}")
                .buildAndExpand(createdEntity.getId())
                .toUri();
        return ResponseEntity.created(locationOfCreatedEntity).build();
    }

    @PutMapping("/{instructorId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    public ResponseEntity<Void> updateInstructor(@PathVariable Integer instructorId, @RequestBody InstructorRequest request, Authentication auth){
        if(isAuthorized(auth, instructorId)){
            InstructorDTO instructorDTO = InstructorConverter.toDTO(
                    InstructorConverter.toEntity(request)
            );
            instructorDTO.setId(instructorId);
            InstructorDTO response = this.instructorService.update(instructorDTO);
            return response!=null? ResponseEntity.noContent().build():
                    ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }
    @DeleteMapping("/{instructorId}/delete")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    public ResponseEntity<Void> deleteInstructor(@PathVariable Integer instructorId, Authentication auth){
        if(isAuthorized(auth, instructorId)){
            this.instructorService.delete(instructorService.findById(instructorId));
            return  ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    //Get all plans for a given instructor.
    @GetMapping("/{instructorId}/plans")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT', 'ROLE_INSTRUCTOR')")
    public ResponseEntity<List<PlanDTO>> getAllPlansForGivenInstructor(@PathVariable Integer instructorId,
                                                                       @RequestParam(required = false) String name,
                                                                       @RequestParam(required = false) String sort,
                                                                       @RequestParam(required = false) Integer pageNumber,
                                                                       @RequestParam(required = false) Integer pageSize){
        if(name != null){
            name = name.replace("%20"," ");
        }
        Filter nameFilter = new Filter("name", name, "LIKE", null, pageNumber, pageSize);

        if(sort != null){
            String[] sortValue = sort.split(":");
            if(Objects.equals(sortValue[0], "name")){
                nameFilter.setSort(sortValue[1]);
            } else {
                throw new RuntimeException("Invalid sort field.");
            }
        }

        List<PlanDTO> plans = instructorService.findAllPlansByInstructorId(instructorId,nameFilter);
        if(plans == null){
            throw new ResourceNotFoundException("Plans not found.");
        }
        return ResponseEntity.ok(plans);
    }

    //Get all clients for a given instructor.
    @GetMapping("/{instructorId}/clients")
    @TrackExecutionTime
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    public ResponseEntity<List<ClientDTO>> getAllClientsForGivenInstructor(@PathVariable Integer instructorId,
                                                                           @RequestParam(required = false) String name,
                                                                           @RequestParam(required = false) String planId,
                                                                           @RequestParam(required = false) String sort,
                                                                           @RequestParam(required = false) Integer pageNumber,
                                                                           @RequestParam(required = false) Integer pageSize,
                                                                           Authentication auth){
        if(isAuthorized(auth, instructorId)){
            if(name != null){
                name = name.replace("%20"," ");
            }
            Filter nameFilter = new Filter("name", name, "LIKE", null,pageNumber,pageSize);
            Filter planFilter = new Filter("id", planId, "=", null, pageNumber, pageSize);
            if(sort != null){
                String[] sortValue = sort.split(":");
                if(Objects.equals(sortValue[0], "name")){
                    nameFilter.setSort(sortValue[1]);
                } else if (Objects.equals(sortValue[0], "planId")) {
                    planFilter.setSort(sortValue[1]);
                } else {
                    throw new SortingException("Invalid sort field.");
                }
            }

            List<ClientDTO> clients = instructorService.findAllClientsByInstructorId(instructorId,nameFilter,planFilter);
            if(clients == null){
                throw new ResourceNotFoundException("Clients not found.");
            }
            return ResponseEntity.ok(clients);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    private boolean isAuthorized(Authentication authentication, Integer instructorId) {
        String authenticatedUsername = authentication.getName();
        if(authenticatedUsername.equals("admin")){
            return true;
        }
        User user = (User) userService.loadUserByUsername(authenticatedUsername);
        InstructorDTO instructorDTO = instructorService.findById(user.getInstructor().getId());
        return Objects.equals(instructorDTO.getId(), instructorId);
    }

}