package com.gym.controller;

import com.gym.domain.dto.ClientDTO;
import com.gym.domain.dto.ClientRequest;
import com.gym.domain.dto.InstructorDTO;
import com.gym.domain.dto.PlanDTO;
import com.gym.domain.entity.Difficulty;
import com.gym.domain.filter.Filter;
import com.gym.service.ClientService;
import com.gym.service.PlanService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ClientControllerTest {
    @InjectMocks
    private ClientController clientController;

    @Mock
    private ClientService clientService;

    @Mock
    private PlanService planService;

    @Test
    public void testFindAllClients() {

        List<ClientDTO> clientDTOList = new ArrayList<>();
        clientDTOList.add(new ClientDTO(1, "New Client", "test@email.com",
                LocalDate.of(1999, 4, 2), LocalDate.of(2023,11,11),
                64.5f, 169.8f, new PlanDTO(1, "test", new InstructorDTO(1, "ins test"), Difficulty.EASY)));

        Mockito.when(clientService.findAll(Mockito.any(Filter.class), Mockito.any(Filter.class)))
                .thenReturn(clientDTOList);

        ResponseEntity<List<ClientDTO>> responseEntity = clientController.findAll(null, null, null, null, null);

        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
        Assertions.assertEquals(clientDTOList, responseEntity.getBody());
    }

    @Test
    public void testFindClientById() {

        ClientDTO clientDTO = new ClientDTO(1, "New Client", "test@email.com",
                LocalDate.of(1999, 4, 2), LocalDate.of(2023,11,11),
                64.5f, 169.8f, new PlanDTO(1, "test", new InstructorDTO(1, "ins test"), Difficulty.EASY));

        Mockito.when(clientService.findById(Mockito.anyInt()))
                .thenReturn(clientDTO);

        ResponseEntity<ClientDTO> responseEntity = clientController.findById(1);

        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
        Assertions.assertEquals(clientDTO, responseEntity.getBody());
    }

    @Test
    public void testCreateClient() {
        ClientRequest clientRequest = new ClientRequest("New Client", "test@email.com",
                LocalDate.of(1999, 4, 2), LocalDate.of(2023,11,11),
                64.5f, 169.8f, 1);

        Mockito.when(clientService.save(Mockito.any(ClientRequest.class)))
                .thenReturn(new ClientDTO());

        ResponseEntity<Void> responseEntity = clientController.createClient(clientRequest, UriComponentsBuilder.newInstance());

        Assertions.assertEquals(201, responseEntity.getStatusCodeValue());
        Assertions.assertEquals(null, responseEntity.getBody());
    }

    @Test
    public void testUpdateClient() {
        ClientRequest clientRequest = new ClientRequest("New Client", "test@email.com",
                LocalDate.of(1999, 4, 2), LocalDate.of(2023,11,11),
                64.5f, 169.8f, 1);
        PlanDTO plan = new PlanDTO(1,"plan test", new InstructorDTO(1,"enola"), Difficulty.MEDIUM);

        Mockito.when(planService.findById(Mockito.anyInt())).thenReturn(plan);
        Mockito.when(clientService.update(Mockito.any(ClientDTO.class)))
                .thenReturn(new ClientDTO());

        ResponseEntity<Void> responseEntity = clientController.updateClient(1, clientRequest);

        Assertions.assertEquals(204, responseEntity.getStatusCodeValue());
        Assertions.assertEquals(null, responseEntity.getBody());
    }

}
