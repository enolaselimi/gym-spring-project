package com.gym.controller;

import com.gym.aspect.TrackExecutionTime;
import com.gym.domain.dto.*;
import com.gym.domain.entity.Exercise;
import com.gym.domain.entity.Instructor;
import com.gym.domain.entity.Plan;
import com.gym.domain.entity.User;
import com.gym.domain.exception.ResourceNotFoundException;
import com.gym.domain.exception.SortingException;
import com.gym.domain.filter.Filter;
import com.gym.domain.mapper.ExerciseConverter;
import com.gym.domain.mapper.InstructorConverter;
import com.gym.domain.mapper.PlanConverter;
import com.gym.domain.mapper.PlanExerciseConverter;
import com.gym.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.naming.AuthenticationNotSupportedException;
import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/plans")
public class PlanController {
    @Autowired
    private PlanService planService;
    @Autowired
    private PlanExerciseService planExerciseService;
    @Autowired
    private InstructorService instructorService;
    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private UserService userService;

    @GetMapping
    @TrackExecutionTime
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT', 'ROLE_INSTRUCTOR')")
    public ResponseEntity<List<PlanDTO>> findAll(@RequestParam(required = false) String name,
                                                 @RequestParam(required = false) String difficulty,
                                                 @RequestParam(required = false) String sort,
                                                 @RequestParam(required = false) Integer pageNumber,
                                                 @RequestParam(required = false) Integer pageSize){
        if(name != null){
            name = name.replace("%20"," ");
        }
        Filter nameFilter = new Filter("name", name, "LIKE", null,pageNumber,pageSize);
        if(difficulty != null){
            difficulty = difficulty.toUpperCase();
        }
        Filter difficultyFilter = new Filter("difficulty", difficulty, "LIKE", null,pageNumber,pageSize);
        if(sort != null){
            String[] sortValue = sort.split(":");
            if(Objects.equals(sortValue[0], "name")){
                nameFilter.setSort(sortValue[1]);
            } else if (Objects.equals(sortValue[0], "difficulty")) {
                difficultyFilter.setSort(sortValue[1]);
            } else {
                throw new SortingException("Invalid sort field.");
            }
        }

        List<PlanDTO> plans = planService.findAll(nameFilter,difficultyFilter);
        if(plans == null){
            throw new ResourceNotFoundException("Plans not found.");
        }
        return ResponseEntity.ok(plans);
    }

    @GetMapping("/{planId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<PlanDTO> findById(@PathVariable Integer planId){
        PlanDTO planDTO = planService.findById(planId);
        return ResponseEntity.ok(planDTO);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    public ResponseEntity<Void> createPlan(@RequestBody @Valid PlanRequest planRequest,
                                             UriComponentsBuilder ucb, Authentication auth){
        User user = (User) userService.loadUserByUsername(auth.getName());
        planRequest.setInstructor_id(user.getInstructor().getId());
        var createdPlan = planService.save(planRequest);
        URI locationOfCreatedPlan = ucb
                .path("api/plans/{planId}")
                .buildAndExpand(createdPlan.getId())
                .toUri();
        return ResponseEntity.created(locationOfCreatedPlan).build();
    }

    @PutMapping("/{planId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    public ResponseEntity<Void> updatePlan(@PathVariable Integer planId, @RequestBody @Valid PlanRequest request,
                                           Authentication auth){
        if(isAuthorized(auth,planId)){
            Instructor instructor = InstructorConverter.fromDTOtoEntity(instructorService.findById(request.getInstructor_id()));
            PlanDTO planToUpdate = PlanConverter.toDTO(
                    PlanConverter.toEntity(request, instructor)
            );
            planToUpdate.setId(planId);
            PlanDTO response = this.planService.update(planToUpdate);
            return response!=null? ResponseEntity.noContent().build():
                    ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }
    @DeleteMapping("/{planId}/delete")
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR', 'ROLE_ADMIN')")
    public ResponseEntity<Void> deletePlan(@PathVariable Integer planId, Authentication auth){
        if(isAuthorized(auth, planId)){
            this.planService.delete(planService.findById(planId));
            return  ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    //Get all exercises within a plan.
    @TrackExecutionTime
    @GetMapping("/{planId}/exercises")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR', 'ROLE_CLIENT')")
    public ResponseEntity<List<PlanExerciseDTO>> getPlanExercises(@PathVariable Integer planId,
                                                                  @RequestParam(required = false) String day,
                                                                  @RequestParam(required = false) String sets,
                                                                  @RequestParam(required = false) String reps,
                                                                  @RequestParam(required = false) String sort,
                                                                  @RequestParam(required = false) Integer pageNumber,
                                                                  @RequestParam(required = false) Integer pageSize){
        if(day != null){
            day = day.replace("%20"," ");
        }
        Filter dayFilter = new Filter("day", day, "LIKE", null,pageNumber,pageSize);
        Filter setsFilter = new Filter("sets", sets, "=", null,pageNumber,pageSize);
        Filter repsFilter = new Filter("reps", reps, "=", null,pageNumber,pageSize);

        if(sort != null){
            String[] sortValue = sort.split(":");
            if(Objects.equals(sortValue[0], "name")){
                dayFilter.setSort(sortValue[1]);
            } else {
                throw new RuntimeException("Invalid sort field.");
            }
        }

        List<PlanExerciseDTO> exerciseDTOS = planExerciseService.findPlanExercises(planId,dayFilter,setsFilter,repsFilter);
        if(exerciseDTOS == null){
            throw new ResourceNotFoundException("Exercises not found.");
        }
        return ResponseEntity.ok(exerciseDTOS);
    }

    //Get which instructor created a plan.
    @GetMapping("/{planId}/instructor")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<InstructorDTO> getPlanInstructor(@PathVariable Integer planId){
        InstructorDTO instructorDTO = planService.findInstructorByPlanId(planId);
        if(instructorDTO == null){
            throw new ResourceNotFoundException("Instructor not found.");
        }
        return ResponseEntity.ok(instructorDTO);
    }

    //Add exercise to a plan.
    @PostMapping("/exercise")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    public ResponseEntity<Void> addExerciseToGivenPlan(@RequestBody PlanExerciseRequest planExerciseRequest,
                                                       UriComponentsBuilder ucb, Authentication auth){
        if(isAuthorized(auth, planExerciseRequest.getPlan_id())){
            PlanExerciseDTO planExerciseDTO = planExerciseService.save(planExerciseRequest);
            URI locationOfUpdatedPlan = ucb
                    .path("api/plans/{planId}")
                    .buildAndExpand(planExerciseDTO.getPlanDTO().getId())
                    .toUri();
            return ResponseEntity.created(locationOfUpdatedPlan).build();
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    //Remove exercise from a plan.
    @DeleteMapping("/{planId}/{exerciseId}/delete")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    public ResponseEntity<Void> deletePlanExercise(@PathVariable Integer planId,
                                                   @PathVariable Integer exerciseId,
                                                   Authentication auth){
        if(isAuthorized(auth, planId)){
            this.planExerciseService.delete(planExerciseService.findById(planId, exerciseId));
            return  ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    //Change exercise info on a given plan.
    @PutMapping("/{planId}/{exerciseId}/update")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    public ResponseEntity<Void> changeExerciseInfoForGivenPlan(@PathVariable Integer planId,
                                                               @PathVariable Integer exerciseId,
                                                               @RequestBody @Valid PlanExerciseRequest planExerciseRequest,
                                                               Authentication auth) {
        if(isAuthorized(auth, planId)){
            planExerciseRequest.setPlan_id(planId);
            planExerciseRequest.setExercise_id(exerciseId);
            Plan plan = PlanConverter.fromDTOtoEntity(planService.findById(planId));
            Exercise exercise = ExerciseConverter.fromDTOtoEntity(exerciseService.findById(exerciseId));
            PlanExerciseDTO planExerciseToUpdate = PlanExerciseConverter.toDTO(
                    PlanExerciseConverter.toEntity(planExerciseRequest, plan, exercise)
            );
            PlanExerciseDTO response = planExerciseService.update(planExerciseToUpdate);
            return response!=null? ResponseEntity.noContent().build():
                    ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    private boolean isAuthorized(Authentication authentication, Integer planId) {
        String authenticatedUsername = authentication.getName();
        if(authenticatedUsername.equals("admin")){
            return true;
        }
        InstructorDTO instructor = planService.findById(planId).getInstructorDTO();
        User user = (User) userService.loadUserByUsername(authenticatedUsername);
        InstructorDTO instructorDTO = instructorService.findById(user.getInstructor().getId());
        return Objects.equals(instructorDTO.getId(), instructor.getId());
    }

}