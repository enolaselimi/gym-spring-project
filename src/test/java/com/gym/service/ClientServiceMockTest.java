package com.gym.service;

import com.gym.domain.dto.*;
import com.gym.domain.entity.Client;
import com.gym.domain.entity.Difficulty;
import com.gym.domain.entity.Instructor;
import com.gym.domain.entity.Plan;
import com.gym.domain.mapper.ClientConverter;
import com.gym.domain.mapper.InstructorConverter;
import com.gym.domain.mapper.PlanConverter;
import com.gym.repository.ClientRepository;
import com.gym.repository.PlanRepository;
import com.gym.service.impl.ClientServiceImpl;
import com.gym.service.impl.PlanServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ClientServiceMockTest {
    @InjectMocks
    private ClientService clientService = Mockito.spy(new ClientServiceImpl());

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PlanService planService;

    @Test
    public void test_findAllClients(){
        List<Client> clientList = new ArrayList<>();
        Client c = new Client("Test", "test@email.com", LocalDate.of(1999, 4, 2), LocalDate.of(2023,11,11), 64.5f, 169.8f,
                new Plan("test", new Instructor("enola"), Difficulty.EASY));
        clientList.add(c);
        Mockito.doReturn(clientList).when(clientRepository).findAll();

        var out = clientService.findAll();
        Mockito.verify(clientRepository).findAll();
        Assertions.assertAll(
                () -> Assertions.assertEquals("Test", out.get(0).getName()),
                () -> Assertions.assertEquals("test@email.com", out.get(0).getEmail()),
                () -> Assertions.assertEquals(LocalDate.of(1999, 4, 2), out.get(0).getDateOfBirth()),
                () -> Assertions.assertEquals(LocalDate.of(2023,11,11), out.get(0).getDateJoined()),
                () -> Assertions.assertEquals(64.5f, out.get(0).getWeight()),
                () -> Assertions.assertEquals(169.8f, out.get(0).getHeight()),
                () -> Assertions.assertEquals("test", out.get(0).getPlanDTO().getName()),
                () -> Assertions.assertEquals("enola", out.get(0).getPlanDTO().getInstructorDTO().getName()),
                () -> Assertions.assertEquals(Difficulty.EASY, out.get(0).getPlanDTO().getDifficulty())
        );

    }

    @Test
    public void test_findById(){
        Client c = new Client("Test", "test@email.com", LocalDate.of(1999, 4, 2), LocalDate.of(2023,11,11), 64.5f, 169.8f,
                new Plan("test", new Instructor("enola"), Difficulty.EASY));
        Mockito.doReturn(c).when(clientRepository).findById(Mockito.anyInt());

        var out = clientService.findById(1);
        Mockito.verify(clientRepository).findById(1);
    }

    @Test
    public void test_saveClient(){
        ClientRequest clientRequestToSave = new ClientRequest("New Client", "test@email.com",
                "1999-04-02", "2023-11-11", 64.5f, 169.8f, 1);
        PlanDTO planDTO = new PlanDTO(1, "test", new InstructorDTO(1, "ins test"),Difficulty.EASY);
        Client convertedClientToSave = ClientConverter.toEntity(clientRequestToSave,
                PlanConverter.fromDTOtoEntity(planDTO));

        Mockito.when(planService.findById(clientRequestToSave.getPlan_id())).thenReturn(planDTO);
        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenReturn(convertedClientToSave);

        ClientDTO savedClient = clientService.save(clientRequestToSave);

        Mockito.verify(planService).findById(clientRequestToSave.getPlan_id());
        Mockito.verify(clientRepository).save(Mockito.any(Client.class));

        Assertions.assertAll(
                () -> Assertions.assertNotNull(savedClient),
                () -> Assertions.assertEquals("New Client", savedClient.getName()),
                () -> Assertions.assertEquals("test@email.com", savedClient.getEmail()),
                () -> Assertions.assertEquals(LocalDate.of(1999,04,02), savedClient.getDateOfBirth()),
                () -> Assertions.assertEquals(LocalDate.of(2023,11,11), savedClient.getDateJoined()),
                () -> Assertions.assertEquals(64.5f, savedClient.getWeight()),
                () -> Assertions.assertEquals(169.8f, savedClient.getHeight()),
                () -> Assertions.assertEquals("test", savedClient.getPlanDTO().getName()),
                () -> Assertions.assertEquals("ins test", savedClient.getPlanDTO().getInstructorDTO().getName()),
                () -> Assertions.assertEquals(Difficulty.EASY, savedClient.getPlanDTO().getDifficulty())
        );
    }

    @Test
    public void test_UpdateClient() {
        ClientDTO clientDTOToUpdate = new ClientDTO(1, "New Client", "test@email.com",
                LocalDate.of(1999, 4, 2), LocalDate.of(2023,11,11),
                64.5f, 169.8f, new PlanDTO(1, "test", new InstructorDTO(1, "ins test"), Difficulty.EASY));
        Client clientEntityToUpdate = ClientConverter.fromDTOtoEntity(clientDTOToUpdate);

        Mockito.when(clientRepository.update(Mockito.any(Client.class))).thenReturn(clientEntityToUpdate);

        ClientDTO updatedClientDTO = clientService.update(clientDTOToUpdate);
        Mockito.verify(clientRepository).update(Mockito.any(Client.class));

        Assertions.assertAll(
                () -> Assertions.assertNotNull(updatedClientDTO),
                () -> Assertions.assertEquals("New Client", updatedClientDTO.getName()),
                () -> Assertions.assertEquals("test@email.com", updatedClientDTO.getEmail()),
                () -> Assertions.assertEquals(LocalDate.of(1999, 4, 2), updatedClientDTO.getDateOfBirth()),
                () -> Assertions.assertEquals(LocalDate.of(2023,11,11), updatedClientDTO.getDateJoined()),
                () -> Assertions.assertEquals(64.5f, updatedClientDTO.getWeight()),
                () -> Assertions.assertEquals(169.8f, updatedClientDTO.getHeight()),
                () -> Assertions.assertEquals("test", updatedClientDTO.getPlanDTO().getName()),
                () -> Assertions.assertEquals("ins test", updatedClientDTO.getPlanDTO().getInstructorDTO().getName()),
                () -> Assertions.assertEquals(Difficulty.EASY, updatedClientDTO.getPlanDTO().getDifficulty())
        );
    }

    @Test
    public void test_DeleteClient() {

        ClientDTO clientDTOToDelete = new ClientDTO(1, "New Client", "test@email.com",
                LocalDate.of(1999, 4, 2), LocalDate.of(2023,11,11),
                64.5f, 169.8f, new PlanDTO(1, "test", new InstructorDTO(1, "ins test"), Difficulty.EASY));
        Client clientEntityToDelete = ClientConverter.fromDTOtoEntity(clientDTOToDelete);

        Mockito.when(clientRepository.delete(Mockito.any(Client.class))).thenReturn(clientEntityToDelete);

        ClientDTO deletedClientDTO = clientService.delete(clientDTOToDelete);

        Mockito.verify(clientRepository).delete(Mockito.any(Client.class));

        Assertions.assertAll(
                () -> Assertions.assertEquals(clientDTOToDelete.getName(), deletedClientDTO.getName()),
                () -> Assertions.assertEquals(clientDTOToDelete.getEmail(), deletedClientDTO.getEmail()),
                () -> Assertions.assertEquals(clientDTOToDelete.getDateOfBirth(), deletedClientDTO.getDateOfBirth()),
                () -> Assertions.assertEquals(clientDTOToDelete.getDateJoined(), deletedClientDTO.getDateJoined()),
                () -> Assertions.assertEquals(clientDTOToDelete.getWeight(), deletedClientDTO.getWeight()),
                () -> Assertions.assertEquals(clientDTOToDelete.getHeight(), deletedClientDTO.getHeight()),
                () -> Assertions.assertEquals(clientDTOToDelete.getPlanDTO().getName(), deletedClientDTO.getPlanDTO().getName()),
                () -> Assertions.assertEquals(clientDTOToDelete.getPlanDTO().getInstructorDTO().getName(), deletedClientDTO.getPlanDTO().getInstructorDTO().getName()),
                () -> Assertions.assertEquals(clientDTOToDelete.getPlanDTO().getDifficulty(), deletedClientDTO.getPlanDTO().getDifficulty())
        );
    }
}
