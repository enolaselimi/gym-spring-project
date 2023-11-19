package com.gym.controller;

import com.gym.aspect.TrackExecutionTime;
import com.gym.domain.dto.*;
import com.gym.domain.entity.Exercise;
import com.gym.domain.entity.Instructor;
import com.gym.domain.entity.Plan;
import com.gym.domain.exception.ResourceNotFoundException;
import com.gym.domain.filter.Filter;
import com.gym.domain.mapper.ExerciseConverter;
import com.gym.domain.mapper.InstructorConverter;
import com.gym.domain.mapper.PlanConverter;
import com.gym.domain.mapper.PlanExerciseConverter;
import com.gym.service.ExerciseService;
import com.gym.service.InstructorService;
import com.gym.service.PlanExerciseService;
import com.gym.service.PlanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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

    @GetMapping
    @TrackExecutionTime
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<List<PlanDTO>> findAll(@RequestParam(required = false) String name,
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
            } else {
                throw new RuntimeException("Invalid sort field.");
            }
        }

        List<PlanDTO> plans = planService.findAll(nameFilter);
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
    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    public ResponseEntity<Void> createPlan(@RequestBody @Valid PlanRequest planRequest,
                                             UriComponentsBuilder ucb){
        var createdPlan = planService.save(planRequest);
        URI locationOfCreatedPlan = ucb
                .path("api/plans/{planId}")
                .buildAndExpand(createdPlan.getId())
                .toUri();
        return ResponseEntity.created(locationOfCreatedPlan).build();
    }

    @PutMapping("/{planId}")
    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    public ResponseEntity<Void> updatePlan(@PathVariable Integer planId, @RequestBody @Valid PlanRequest request){
        Instructor instructor = InstructorConverter.fromDTOtoEntity(instructorService.findById(request.getInstructor_id()));
        PlanDTO planToUpdate = PlanConverter.toDTO(
                PlanConverter.toEntity(request, instructor)
        );
        planToUpdate.setId(planId);
        PlanDTO response = this.planService.update(planToUpdate);
        return response!=null? ResponseEntity.noContent().build():
                ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{planId}/delete")
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR', 'ROLE_ADMIN')")
    public ResponseEntity<Void> deletePlan(@PathVariable Integer planId){
        this.planService.delete(planService.findById(planId));
        return  ResponseEntity.noContent().build();
    }

    //Get all exercises within a plan.
    @TrackExecutionTime
    @GetMapping("/{planId}/exercises")
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR', 'ROLE_CLIENT')")
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
    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    public ResponseEntity<Void> addExerciseToGivenPlan(@RequestBody PlanExerciseRequest planExerciseRequest,
                                                       UriComponentsBuilder ucb){
        PlanExerciseDTO planExerciseDTO = planExerciseService.save(planExerciseRequest);
        URI locationOfUpdatedPlan = ucb
                .path("api/plans/{planId}")
                .buildAndExpand(planExerciseDTO.getPlanDTO().getId())
                .toUri();
        return ResponseEntity.created(locationOfUpdatedPlan).build();
    }

    //Remove exercise from a plan.
    @DeleteMapping("/{planId}/{exerciseId}/delete")
    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    public ResponseEntity<Void> deletePlanExercise(@PathVariable Integer planId,
                                                   @PathVariable Integer exerciseId){
        this.planExerciseService.delete(planExerciseService.findById(planId, exerciseId));
        return  ResponseEntity.noContent().build();
    }

    //Change exercise info on a given plan.
    @PutMapping("/{planId}/{exerciseId}/update")
    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    public ResponseEntity<Void> changeExerciseInfoForGivenPlan(@PathVariable Integer planId,
                                                               @PathVariable Integer exerciseId,
                                                               @RequestBody @Valid PlanExerciseRequest planExerciseRequest) {
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
    }
}