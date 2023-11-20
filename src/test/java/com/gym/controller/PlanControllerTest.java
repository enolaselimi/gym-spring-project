package com.gym.controller;

import com.gym.domain.dto.InstructorDTO;
import com.gym.domain.dto.PlanDTO;
import com.gym.domain.dto.PlanRequest;
import com.gym.domain.entity.Client;
import com.gym.domain.entity.Difficulty;
import com.gym.domain.entity.Instructor;
import com.gym.domain.entity.User;
import com.gym.domain.filter.Filter;
import com.gym.domain.mapper.ClientConverter;
import com.gym.domain.mapper.InstructorConverter;
import com.gym.service.InstructorService;
import com.gym.service.PlanService;
import com.gym.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PlanControllerTest {
    @InjectMocks
    private PlanController planController;

    @Mock
    private PlanService planService;

    @Mock
    private InstructorService instructorService;

    @Mock
    private UserService userService;

    @Test
    public void testFindAllPlans() {

        List<PlanDTO> planDTOList = new ArrayList<>();
        planDTOList.add(new PlanDTO(1,"New Plan", new InstructorDTO(1,"test"), Difficulty.EASY));

        Mockito.lenient().when(planService.findAll(Mockito.any(Filter.class), Mockito.any(Filter.class)))
                .thenReturn(planDTOList);

        ResponseEntity<List<PlanDTO>> responseEntity = planController.findAll(null, null, null, null, null);

        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
        Assertions.assertEquals(planDTOList, responseEntity.getBody());
    }

    @Test
    public void testFindPlanById() {

        PlanDTO planDTO = new PlanDTO(1,"New Plan", new InstructorDTO(1,"test"), Difficulty.EASY);

        Mockito.when(planService.findById(Mockito.anyInt()))
                .thenReturn(planDTO);

        ResponseEntity<PlanDTO> responseEntity = planController.findById(1);

        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
        Assertions.assertEquals(planDTO, responseEntity.getBody());
    }

    @Test
    @WithMockUser(username = "test", roles = {"INSTRUCTOR"})
    public void testCreatePlan() {

        PlanRequest planRequest = new PlanRequest("New Plan", 1, Difficulty.EASY);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("test");
        User user = new User();
        Instructor instructor = new Instructor();
        instructor.setId(1);
        user.setInstructor(instructor);
        Mockito.when(userService.loadUserByUsername("test")).thenReturn(user);
        Mockito.when(planService.save(Mockito.any(PlanRequest.class)))
                .thenReturn(new PlanDTO());

        ResponseEntity<Void> responseEntity = planController.createPlan(planRequest, UriComponentsBuilder.newInstance(),authentication);

        Assertions.assertEquals(201, responseEntity.getStatusCodeValue());
        Assertions.assertEquals(null, responseEntity.getBody());
    }

    @Test
    @WithMockUser(username = "test", roles = {"INSTRUCTOR"})
    public void testUpdatePlan() {

        PlanRequest planRequest = new PlanRequest("New Plan", 1, Difficulty.EASY);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("test");
        User user = new User();
        Instructor instructor = new Instructor();
        instructor.setId(1);
        user.setInstructor(instructor);
        Mockito.when(userService.loadUserByUsername("test")).thenReturn(user);
        Mockito.when(planService.findById(Mockito.anyInt())).thenReturn(new PlanDTO(1,"New Plan",InstructorConverter.toDTO(instructor),Difficulty.EASY));
        Mockito.when(instructorService.findById(Mockito.anyInt())).thenReturn(InstructorConverter.toDTO(instructor));

        Mockito.when(planService.update(Mockito.any(PlanDTO.class)))
                .thenReturn(new PlanDTO());

        ResponseEntity<Void> responseEntity = planController.updatePlan(1, planRequest,authentication);

        Assertions.assertEquals(204, responseEntity.getStatusCodeValue());
        Assertions.assertEquals(null, responseEntity.getBody());
    }

}
