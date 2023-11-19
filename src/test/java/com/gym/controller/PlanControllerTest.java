package com.gym.controller;

import com.gym.domain.dto.InstructorDTO;
import com.gym.domain.dto.PlanDTO;
import com.gym.domain.dto.PlanRequest;
import com.gym.domain.entity.Difficulty;
import com.gym.domain.filter.Filter;
import com.gym.service.InstructorService;
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


    @Test
    public void testFindAllPlans() {

        List<PlanDTO> planDTOList = new ArrayList<>();
        planDTOList.add(new PlanDTO(1,"New Plan", new InstructorDTO(1,"enola"), Difficulty.EASY));

        Mockito.when(planService.findAll(Mockito.any(Filter.class)))
                .thenReturn(planDTOList);

        ResponseEntity<List<PlanDTO>> responseEntity = planController.findAll(null, null, null, null);

        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
        Assertions.assertEquals(planDTOList, responseEntity.getBody());
    }

    @Test
    public void testFindPlanById() {

        PlanDTO planDTO = new PlanDTO(1,"New Plan", new InstructorDTO(1,"enola"), Difficulty.EASY);

        Mockito.when(planService.findById(Mockito.anyInt()))
                .thenReturn(planDTO);

        ResponseEntity<PlanDTO> responseEntity = planController.findById(1);

        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
        Assertions.assertEquals(planDTO, responseEntity.getBody());
    }

    @Test
    public void testCreatePlan() {

        PlanRequest planRequest = new PlanRequest("New Plan", 1, Difficulty.EASY);

        Mockito.when(planService.save(Mockito.any(PlanRequest.class)))
                .thenReturn(new PlanDTO());

        ResponseEntity<Void> responseEntity = planController.createPlan(planRequest, UriComponentsBuilder.newInstance());

        // Verify the result
        Assertions.assertEquals(201, responseEntity.getStatusCodeValue());
        Assertions.assertEquals(null, responseEntity.getBody());
    }

    @Test
    public void testUpdatePlan() {

        PlanRequest planRequest = new PlanRequest("New Plan", 1, Difficulty.EASY);

        InstructorDTO instructorDTO = new InstructorDTO(1, "test");
        Mockito.when(instructorService.findById(Mockito.anyInt())).thenReturn(instructorDTO);

        Mockito.when(planService.update(Mockito.any(PlanDTO.class)))
                .thenReturn(new PlanDTO());

        ResponseEntity<Void> responseEntity = planController.updatePlan(1, planRequest);

        Assertions.assertEquals(204, responseEntity.getStatusCodeValue());
        Assertions.assertEquals(null, responseEntity.getBody());
    }

}
