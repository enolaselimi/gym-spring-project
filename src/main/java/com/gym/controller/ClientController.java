package com.gym.controller;

import com.gym.aspect.TrackExecutionTime;
import com.gym.domain.dto.ClientDTO;
import com.gym.domain.dto.ClientRequest;
import com.gym.domain.dto.ExerciseDTO;
import com.gym.domain.dto.PlanDTO;
import com.gym.domain.entity.Plan;
import com.gym.domain.exception.ResourceNotFoundException;
import com.gym.domain.filter.Filter;
import com.gym.domain.mapper.ClientConverter;
import com.gym.domain.mapper.PlanConverter;
import com.gym.service.ClientService;
import com.gym.service.PlanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private PlanService planService;

    @GetMapping
    @TrackExecutionTime
    public ResponseEntity<List<ClientDTO>> findAll(@RequestParam(required = false) String name,
                                                   @RequestParam(required = false) String planId,
                                                   @RequestParam(required = false) String sort,
                                                   @RequestParam(required = false) Integer pageNumber,
                                                   @RequestParam(required = false) Integer pageSize){
        if(name != null){
            name = name.replace("%20"," ");
        }
        Filter nameFilter = new Filter("name", name, "LIKE", null, pageNumber, pageSize);
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

        List<ClientDTO> clients = clientService.findAll(nameFilter, planFilter);
        if(clients == null){
            throw new ResourceNotFoundException("Clients not found.");
        }
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<ClientDTO> findById(@PathVariable Integer clientId){
        ClientDTO clientDTO = clientService.findById(clientId);
        if(clientDTO == null){
            throw new ResourceNotFoundException("Client not found.");
        }
        return ResponseEntity.ok(clientDTO);
    }

    @PostMapping
    public ResponseEntity<Void> createClient(@RequestBody @Valid ClientRequest clientRequest,
                                             UriComponentsBuilder ucb){
        var createdClient = clientService.save(clientRequest);
        URI locationOfCreatedClient = ucb
                .path("api/clients/{clientId}")
                .buildAndExpand(createdClient.getId())
                .toUri();
        return ResponseEntity.created(locationOfCreatedClient).build();
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<Void> updateClient(@PathVariable Integer clientId, @RequestBody @Valid ClientRequest request){
        Plan plan = PlanConverter.fromDTOtoEntity(planService.findById(request.getPlan_id()));
        ClientDTO clientToUpdate = ClientConverter.toDTO(
                ClientConverter.toEntity(request, plan)
        );
        clientToUpdate.setId(clientId);
        ClientDTO response = this.clientService.update(clientToUpdate);
        return response!=null? ResponseEntity.noContent().build():
                ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{clientId}/delete")
    public ResponseEntity<Void> deleteClient(@PathVariable Integer clientId){
        this.clientService.delete(clientService.findById(clientId));
        return  ResponseEntity.noContent().build();
    }


    //Get which plan a specific client is subscribed to.
    @GetMapping("{clientId}/plan")
    public ResponseEntity<PlanDTO> getClientPlan(@PathVariable Integer clientId){
        PlanDTO planDTO = clientService.findPlanByClientId(clientId);
        if(planDTO == null){
            throw  new ResourceNotFoundException("Plan not found.");
        }
        return ResponseEntity.ok(planDTO);
    }

    //Get all exercises a specific client has to do.
    @GetMapping("/{clientId}/exercises")
    public ResponseEntity<List<ExerciseDTO>> findAllExercisesForGivenClient(@PathVariable Integer clientId,
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
            }
        }
        List<ExerciseDTO> exercises = clientService.findAllExercises(clientId, nameFilter);
        if(exercises == null){
            throw new ResourceNotFoundException("Exercises not found.");
        }
        return ResponseEntity.ok(exercises);
    }

    //Subscribe client to a new plan.
    @PutMapping("/{clientId}/subscribe/{planId}")
    public ResponseEntity<Void> subscribeToNewPlan(@PathVariable Integer clientId,
                                                   @PathVariable Integer planId){
        ClientDTO clientFound = clientService.findById(clientId);
        ClientDTO clientDTO = new ClientDTO(clientId, clientFound.getName(), clientFound.getEmail(),
                clientFound.getDateOfBirth(), clientFound.getDateJoined(), clientFound.getWeight(),
                clientFound.getHeight(), planService.findById(planId));
        return clientService.update(clientDTO)!=null? ResponseEntity.noContent().build():
                ResponseEntity.notFound().build();
    }
}