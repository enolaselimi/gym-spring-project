package com.gym.service;

import com.gym.domain.dto.InstructorDTO;
import com.gym.domain.dto.InstructorRequest;
import com.gym.domain.dto.PlanDTO;
import com.gym.domain.dto.PlanRequest;
import com.gym.domain.entity.Difficulty;
import com.gym.domain.entity.Instructor;
import com.gym.domain.entity.Plan;
import com.gym.domain.entity.PlanExercise;
import com.gym.domain.mapper.InstructorConverter;
import com.gym.domain.mapper.PlanConverter;
import com.gym.repository.PlanRepository;
import com.gym.service.impl.PlanServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.xmlunit.diff.Diff;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PlanServiceMockitoTest {
    @InjectMocks
    private PlanService planService = Mockito.spy(new PlanServiceImpl());

    @Mock
    private PlanRepository planRepository;
    @Mock
    private InstructorService instructorService;

    @Test
    public void test_findAllPlans(){
        List<Plan> planList = new ArrayList<>();
        Plan p = new Plan("plan test", new Instructor("enola"), Difficulty.MEDIUM);
        planList.add(p);
        Mockito.doReturn(planList).when(planRepository).findAll();

        var out = planService.findAll();
        Mockito.verify(planRepository).findAll();
        Assertions.assertAll(
                () -> Assertions.assertEquals("plan test", out.get(0).getName()),
                () -> Assertions.assertEquals("enola", out.get(0).getInstructorDTO().getName()),
                () -> Assertions.assertEquals(Difficulty.MEDIUM, out.get(0).getDifficulty())
        );

    }

    @Test
    public void test_findById(){
        Plan p = new Plan("plan test", new Instructor("enola"), Difficulty.MEDIUM);
        Mockito.doReturn(p).when(planRepository).findById(Mockito.anyInt());

        var out = planService.findById(1);
        Mockito.verify(planRepository).findById(1);
    }

    @Test
    public void test_savePlan(){
        PlanRequest planRequestToSave = new PlanRequest("New Plan", 1, Difficulty.EASY);
        InstructorDTO instructorDTO = new InstructorDTO(1,"enola");
        Plan planToSave = PlanConverter.toEntity(planRequestToSave, InstructorConverter.fromDTOtoEntity(instructorDTO));


        Mockito.when(instructorService.findById(planRequestToSave.getInstructor_id())).thenReturn(instructorDTO);
        Mockito.when(planRepository.save(Mockito.any(Plan.class))).thenReturn(planToSave);

        PlanDTO savedPlan = planService.save(planRequestToSave);

        Mockito.verify(instructorService).findById(planRequestToSave.getInstructor_id());
        Mockito.verify(planRepository).save(Mockito.any(Plan.class));


        Assertions.assertAll(
                () -> Assertions.assertNotNull(savedPlan),
                () -> Assertions.assertEquals("New Plan", savedPlan.getName()),
                () -> Assertions.assertEquals("enola", savedPlan.getInstructorDTO().getName()),
                () -> Assertions.assertEquals(Difficulty.EASY, savedPlan.getDifficulty())
        );
    }

    @Test
    public void test_UpdatePlan() {
        PlanDTO planDTOToUpdate = new PlanDTO(1,"New Plan", new InstructorDTO(1,"enola"), Difficulty.EASY);
        Plan planEntityToUpdate = PlanConverter.fromDTOtoEntity(planDTOToUpdate);

        Mockito.when(planRepository.update(Mockito.any(Plan.class))).thenReturn(planEntityToUpdate);

        PlanDTO updatedPlanDTO = planService.update(planDTOToUpdate);
        Mockito.verify(planRepository).update(Mockito.any(Plan.class));

        Assertions.assertAll(
                () -> Assertions.assertNotNull(updatedPlanDTO),
                () -> Assertions.assertEquals(planDTOToUpdate.getName(), updatedPlanDTO.getName()),
                () -> Assertions.assertEquals(planDTOToUpdate.getInstructorDTO().getName(), updatedPlanDTO.getInstructorDTO().getName()),
                () -> Assertions.assertEquals(planDTOToUpdate.getDifficulty(), updatedPlanDTO.getDifficulty())
        );
    }

    @Test
    public void test_DeletePlan() {

        PlanDTO planDTOToDelete = new PlanDTO(1,"New Plan", new InstructorDTO(1,"enola"), Difficulty.EASY);
        Plan planEntityToDelete = PlanConverter.fromDTOtoEntity(planDTOToDelete);

        Mockito.when(planRepository.delete(Mockito.any(Plan.class))).thenReturn(planEntityToDelete);

        PlanDTO deletedPlanDTO = planService.delete(planDTOToDelete);

        Mockito.verify(planRepository).delete(Mockito.any(Plan.class));

        Assertions.assertAll(
                () -> Assertions.assertEquals(planDTOToDelete.getName(), deletedPlanDTO.getName()),
                () -> Assertions.assertEquals(planDTOToDelete.getInstructorDTO().getName(), deletedPlanDTO.getInstructorDTO().getName()),
                () -> Assertions.assertEquals(planDTOToDelete.getDifficulty(), deletedPlanDTO.getDifficulty())
        );
    }
}