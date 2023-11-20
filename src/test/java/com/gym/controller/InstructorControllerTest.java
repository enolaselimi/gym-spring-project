package com.gym.controller;

import com.gym.domain.dto.ClientDTO;
import com.gym.domain.dto.InstructorDTO;
import com.gym.domain.dto.PlanDTO;
import com.gym.domain.entity.Client;
import com.gym.domain.entity.Difficulty;
import com.gym.domain.entity.Instructor;
import com.gym.domain.entity.User;
import com.gym.domain.filter.Filter;
import com.gym.domain.mapper.ClientConverter;
import com.gym.domain.mapper.InstructorConverter;
import com.gym.service.InstructorService;
import com.gym.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class InstructorControllerTest {
    @InjectMocks
    private InstructorController instructorController;

    @Mock
    private InstructorService instructorService;

    @Mock
    private UserService userService;

    @Test
    public void testFindAllInstructors() {

        List<InstructorDTO> instructorDTOList = new ArrayList<>();
        instructorDTOList.add(new InstructorDTO(1, "test"));

        Mockito.when(instructorService.findAll(Mockito.any(Filter.class)))
                .thenReturn(instructorDTOList);


        ResponseEntity<List<InstructorDTO>> responseEntity = instructorController.findAll(null, null, null, null);


        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
        Assertions.assertEquals(instructorDTOList.get(0).getName(), responseEntity.getBody().get(0).getName());
    }


    @Test
    public void testGetAllPlansForGivenInstructor() {

        List<PlanDTO> planDTOList = new ArrayList<>();
        planDTOList.add(new PlanDTO(1, "test", new InstructorDTO(1, "ins test"), Difficulty.EASY));

        Mockito.when(instructorService.findAllPlansByInstructorId(Mockito.anyInt(), Mockito.any(Filter.class)))
                .thenReturn(planDTOList);

        ResponseEntity<List<PlanDTO>> responseEntity = instructorController.getAllPlansForGivenInstructor(1, null, null, null, null);

        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
        Assertions.assertEquals(planDTOList, responseEntity.getBody());
    }

    @Test
    @WithMockUser(username = "test", roles = {"INSTRUCTOR"})
    public void testGetAllClientsForGivenInstructor() {

        List<ClientDTO> clientDTOList = new ArrayList<>();
        clientDTOList.add(new ClientDTO(1, "New Client", "test@email.com",
                LocalDate.of(1999, 4, 2), LocalDate.of(2023,11,11),
                64.5f, 169.8f, new PlanDTO(1, "test", new InstructorDTO(1, "ins test"), Difficulty.EASY)));
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("test");
        User user = new User();
        Instructor instructor = new Instructor();
        instructor.setId(1);
        user.setInstructor(instructor);
        Mockito.when(userService.loadUserByUsername("test")).thenReturn(user);
        Mockito.when(instructorService.findById(Mockito.anyInt())).thenReturn(InstructorConverter.toDTO(instructor));
        Mockito.when(instructorService.findAllClientsByInstructorId(Mockito.anyInt(), Mockito.any(Filter.class), Mockito.any(Filter.class)))
                .thenReturn(clientDTOList);

        ResponseEntity<List<ClientDTO>> responseEntity = instructorController.getAllClientsForGivenInstructor(1, null, null, null, null, null, authentication);

        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
        Assertions.assertEquals(clientDTOList, responseEntity.getBody());
    }
}
