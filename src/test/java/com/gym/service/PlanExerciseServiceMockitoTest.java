package com.gym.service;

import com.gym.domain.entity.*;
import com.gym.repository.PlanExerciseRepository;
import com.gym.service.impl.PlanExerciseServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PlanExerciseServiceMockitoTest {
    @InjectMocks
    private PlanExerciseService planExerciseService = Mockito.spy(new PlanExerciseServiceImpl());

    @Mock
    private PlanExerciseRepository planExerciseRepository;

    @Test
    public void test_findAllPlanExercises(){
        List<PlanExercise> planExerciseList = new ArrayList<>();
        PlanExercise p_e = new PlanExercise(new Plan("test",new Instructor(), Difficulty.EASY),
                new Exercise("ex test", Difficulty.EASY),3,10,"Monday");
        planExerciseList.add(p_e);
        Mockito.doReturn(planExerciseList).when(planExerciseRepository).findAll();

        var out = planExerciseService.findAll();
        Mockito.verify(planExerciseRepository).findAll();
        Assertions.assertAll(
                () -> Assertions.assertEquals("test", planExerciseList.get(0).getPlan().getName()),
                () -> Assertions.assertEquals("ex test", planExerciseList.get(0).getExercise().getName()),
                () -> Assertions.assertEquals(3, planExerciseList.get(0).getSets()),
                () -> Assertions.assertEquals(10, planExerciseList.get(0).getReps()),
                () -> Assertions.assertEquals("Monday", planExerciseList.get(0).getDay())
        );
    }

    @Test
    public void test_findPlanExerciseById(){
        PlanExercise p_e = new PlanExercise(new Plan("test",new Instructor(), Difficulty.EASY),
                new Exercise("ex test", Difficulty.EASY),3,10,"Monday");
        Mockito.doReturn(p_e).when(planExerciseRepository).findById(Mockito.anyInt(), Mockito.anyInt());

        var out = planExerciseService.findById(1,1);
        Mockito.verify(planExerciseRepository).findById(1,1);
    }
}
