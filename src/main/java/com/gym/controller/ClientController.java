package com.gym.controller;

import com.gym.aspect.TrackExecutionTime;
import com.gym.domain.dto.*;
import com.gym.domain.entity.Plan;
import com.gym.domain.entity.User;
import com.gym.domain.exception.ResourceNotFoundException;
import com.gym.domain.exception.SortingException;
import com.gym.domain.filter.Filter;
import com.gym.domain.mapper.ClientConverter;
import com.gym.domain.mapper.PlanConverter;
import com.gym.service.ClientService;
import com.gym.service.PlanService;
import com.gym.service.UserService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/clients")
public class ClientController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private PlanService planService;
    @Autowired
    private UserService userService;

    @GetMapping
    @TrackExecutionTime
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<ClientDTO> findById(@PathVariable Integer clientId, Authentication auth){
        if(isAuthorized(auth,clientId)){
            ClientDTO clientDTO = clientService.findById(clientId);
            if(clientDTO == null){
                throw new ResourceNotFoundException("Client not found.");
            }
            return ResponseEntity.ok(clientDTO);
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<Void> updateClient(@PathVariable Integer clientId, @RequestBody @Valid ClientRequest request,
                                             Authentication auth){
        if(isAuthorized(auth, clientId)){
            Plan plan = PlanConverter.fromDTOtoEntity(planService.findById(request.getPlan_id()));
            ClientDTO clientToUpdate = ClientConverter.toDTO(
                    ClientConverter.toEntity(request, plan)
            );
            clientToUpdate.setId(clientId);
            ClientDTO response = this.clientService.update(clientToUpdate);
            return response!=null? ResponseEntity.noContent().build():
                    ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    @DeleteMapping("/{clientId}/delete")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<Void> deleteClient(@PathVariable Integer clientId, Authentication auth){
        if(isAuthorized(auth, clientId)){
            this.clientService.delete(clientService.findById(clientId));
            return  ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


    //Get which plan a specific client is subscribed to.
    @GetMapping("{clientId}/plan")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<PlanDTO> getClientPlan(@PathVariable Integer clientId, Authentication auth){
        if(isAuthorized(auth, clientId)){
            PlanDTO planDTO = clientService.findPlanByClientId(clientId);
            if(planDTO == null){
                throw  new ResourceNotFoundException("Plan not found.");
            }
            return ResponseEntity.ok(planDTO);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    //Get all exercises a specific client has to do.
    @GetMapping("/{clientId}/exercises")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<List<ExerciseDTO>> findAllExercisesForGivenClient(@PathVariable Integer clientId,
                                                                            @RequestParam(required = false) String name,
                                                                            @RequestParam(required = false) String day,
                                                                            @RequestParam(required = false) String sort,
                                                                            @RequestParam(required = false) Integer pageNumber,
                                                                            @RequestParam(required = false) Integer pageSize,
                                                                            Authentication auth){
        if(isAuthorized(auth,clientId)){
            if(name != null){
                name = name.replace("%20"," ");
            }
            Filter nameFilter = new Filter("name", name, "LIKE", null, pageNumber, pageSize);
            Filter dayFilter = new Filter("day", day, "LIKE", null, pageNumber, pageSize);

            if(sort != null){
                String[] sortValue = sort.split(":");
                if(Objects.equals(sortValue[0], "name")){
                    nameFilter.setSort(sortValue[1]);
                } else if (Objects.equals(sortValue[0], "day")) {
                    dayFilter.setSort(sortValue[1]);
                } else {
                    throw new SortingException("Invalid sort field.");
                }
            }
            List<ExerciseDTO> exercises = clientService.findAllExercises(clientId, nameFilter,dayFilter);
            if(exercises == null){
                throw new ResourceNotFoundException("Exercises not found.");
            }
            return ResponseEntity.ok(exercises);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    //Subscribe client to a new plan.
    @PutMapping("/{clientId}/subscribe/{planId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<Void> subscribeToNewPlan(@PathVariable Integer clientId,
                                                   @PathVariable Integer planId,
                                                   Authentication auth){
        if(isAuthorized(auth, clientId)){
            ClientDTO clientFound = clientService.findById(clientId);
            ClientDTO clientDTO = new ClientDTO(clientId, clientFound.getName(), clientFound.getEmail(),
                    clientFound.getDateOfBirth(), clientFound.getDateJoined(), clientFound.getWeight(),
                    clientFound.getHeight(), planService.findById(planId));
            return clientService.update(clientDTO)!=null? ResponseEntity.noContent().build():
                    ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    private boolean isAuthorized(Authentication authentication, Integer clientId) {
        String authenticatedUsername = authentication.getName();
        if(authenticatedUsername.equals("admin")){
            return true;
        }
        User user = (User) userService.loadUserByUsername(authenticatedUsername);
        ClientDTO clientDTO = clientService.findById(user.getClient().getId());
        return Objects.equals(clientDTO.getId(), clientId);
    }
}