package com.gym.controller;

import com.gym.aspect.TrackExecutionTime;
import com.gym.domain.dto.ExerciseDTO;
import com.gym.domain.dto.ExerciseRequest;
import com.gym.domain.dto.PlanDTO;
import com.gym.domain.exception.ResourceNotFoundException;
import com.gym.domain.filter.Filter;
import com.gym.domain.mapper.ExerciseConverter;
import com.gym.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {
    @Autowired
    private ExerciseService exerciseService;

    @GetMapping
    @TrackExecutionTime
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR', 'ROLE_CLIENT')")
    public ResponseEntity<List<ExerciseDTO>> findAll(@RequestParam(required = false) String name,
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
            } else{
                throw new RuntimeException("Invalid sort field.");
            }
        }

        List<ExerciseDTO> exercises = exerciseService.findAll(nameFilter);
        if(exercises == null){
            throw new ResourceNotFoundException("Exercises not found.");
        }
        return ResponseEntity.ok(exercises);
    }

    @GetMapping("/{exerciseId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR','ROLE_CLIENT')")
    public ResponseEntity<ExerciseDTO> findById(@PathVariable Integer exerciseId){
        ExerciseDTO exerciseDTO = exerciseService.findById(exerciseId);
        if(exerciseDTO == null){
            throw new ResourceNotFoundException("Exercise not found.");
        }
        return ResponseEntity.ok(exerciseDTO);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> createExercise(@RequestBody ExerciseRequest exerciseRequest,
                                             UriComponentsBuilder ucb){
        var createdEntity = exerciseService.save(exerciseRequest);
        URI locationOfCreatedEntity = ucb
                .path("api/exercises/{exerciseId}")
                .buildAndExpand(createdEntity.getId())
                .toUri();
        return ResponseEntity.created(locationOfCreatedEntity).build();
    }

    @PutMapping("/{exerciseId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    public ResponseEntity<Void> updateExercise(@PathVariable Integer exerciseId, @RequestBody ExerciseRequest request){
        ExerciseDTO exerciseDTO = ExerciseConverter.toDTO(
                ExerciseConverter.toEntity(request)
        );
        exerciseDTO.setId(exerciseId);
        ExerciseDTO response = this.exerciseService.update(exerciseDTO);
        return response!=null? ResponseEntity.noContent().build():
                ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{exerciseId}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteExercise(@PathVariable Integer exerciseId){
        this.exerciseService.delete(exerciseService.findById(exerciseId));
        return  ResponseEntity.noContent().build();
    }

    //Get all plans which include a specific exercise.
    @GetMapping("/{exerciseId}/plans")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<PlanDTO>> findAllPlansOfGivenExercise(@PathVariable Integer exerciseId,
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
            } else{
                throw new RuntimeException("Invalid sort field.");
            }
        }
        List<PlanDTO> planDTOS = exerciseService.findPlansByGivenExercise(exerciseId,nameFilter);
        if(planDTOS == null){
            throw new ResourceNotFoundException("Plans not found.");
        }
        return ResponseEntity.ok(planDTOS);
    }

}