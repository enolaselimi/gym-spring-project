package com.gym.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.domain.dto.ExerciseDTO;
import com.gym.domain.entity.Difficulty;
import com.gym.domain.filter.Filter;
import com.gym.service.ExerciseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(ExerciseController.class)
@ExtendWith(SpringExtension.class)
public class ExerciseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ExerciseService exerciseService;


    @Test
    @WithMockUser(username = "admin", password = "password", roles = {"ADMIN"})
    public void test_getALlExercises() throws Exception {
        List<ExerciseDTO> exerciseDTOS = new ArrayList<>();
        ExerciseDTO e = new ExerciseDTO(1,"test",Difficulty.EASY);
        exerciseDTOS.add(e);
        Mockito.doReturn(exerciseDTOS).when(exerciseService).findAll();
        mockMvc.perform(get("/api/exercises")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

        Mockito.verify(exerciseService).findAll(Mockito.any(Filter.class));
        Assertions.assertEquals(1, exerciseDTOS.size());
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = {"ADMIN"})
    public void testFindById() throws Exception {
        ExerciseDTO e = new ExerciseDTO(1,"test",Difficulty.EASY);

        Mockito.when(exerciseService.findById(Mockito.anyInt())).thenReturn(e);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/exercises/{exerciseId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(e.getName()));

        Mockito.verify(exerciseService).findById(1);
    }

}
