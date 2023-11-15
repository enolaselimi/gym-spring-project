package com.gym.controller;

import com.gym.aspect.TrackExecutionTime;
import com.gym.domain.dto.ClientDTO;
import com.gym.domain.dto.InstructorDTO;
import com.gym.domain.dto.InstructorRequest;
import com.gym.domain.dto.PlanDTO;
import com.gym.domain.exception.ResourceNotFoundException;
import com.gym.domain.filter.Filter;
import com.gym.domain.mapper.InstructorConverter;
import com.gym.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    @TrackExecutionTime
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
    public ResponseEntity<InstructorDTO> findById(@PathVariable Integer instructorId){
        InstructorDTO instructorDTO = instructorService.findById(instructorId);
        if(instructorDTO == null){
            throw new ResourceNotFoundException("Exercise not found.");
        }
        return ResponseEntity.ok(instructorDTO);
    }

    @PostMapping
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
    public ResponseEntity<Void> updateInstructor(@PathVariable Integer instructorId, @RequestBody InstructorRequest request){
        InstructorDTO instructorDTO = InstructorConverter.toDTO(
                InstructorConverter.toEntity(request)
        );
        instructorDTO.setId(instructorId);
        InstructorDTO response = this.instructorService.update(instructorDTO);
        return response!=null? ResponseEntity.noContent().build():
                ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{instructorId}/delete")
    public ResponseEntity<Void> deleteInstructor(@PathVariable Integer instructorId){
        this.instructorService.delete(instructorService.findById(instructorId));
        return  ResponseEntity.noContent().build();
    }

    //Get all plans for a given instructor.
    @GetMapping("/{instructorId}/plans")
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
    public ResponseEntity<List<ClientDTO>> getAllClientsForGivenInstructor(@PathVariable Integer instructorId,
                                                                           @RequestParam(required = false) String name,
                                                                           @RequestParam(required = false) String planId,
                                                                           @RequestParam(required = false) String sort,
                                                                           @RequestParam(required = false) Integer pageNumber,
                                                                           @RequestParam(required = false) Integer pageSize){
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
                throw new RuntimeException("Invalid sort field.");
            }
        }

        List<ClientDTO> clients = instructorService.findAllClientsByInstructorId(instructorId,nameFilter,planFilter);
        if(clients == null){
            throw new ResourceNotFoundException("Clients not found.");
        }
        return ResponseEntity.ok(clients);
    }

}