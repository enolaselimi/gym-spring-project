package com.gym.service;

import com.gym.domain.dto.ExerciseDTO;
import com.gym.domain.dto.ExerciseRequest;
import com.gym.domain.entity.Difficulty;
import com.gym.domain.entity.Exercise;
import com.gym.domain.mapper.ExerciseConverter;
import com.gym.repository.ExerciseRepository;
import com.gym.service.impl.ExerciseServiceImpl;
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
public class ExerciseServiceMockitoTest {
    @InjectMocks
    private ExerciseService exerciseService = Mockito.spy(new ExerciseServiceImpl());;

    @Mock
    private ExerciseRepository exerciseRepository;


    @Test
    public void test_findAllExercises(){
        List<Exercise> exerciseList = new ArrayList<>();
        var e = new Exercise("Mountain Climb", Difficulty.MEDIUM);
        exerciseList.add(e);
        Mockito.doReturn(exerciseList).when(exerciseRepository).findAll();


        var out = exerciseService.findAll();

        Mockito.verify(exerciseRepository).findAll();
        Assertions.assertAll(
                () -> Assertions.assertEquals("Mountain Climb",out.get(0).getName()),
                () -> Assertions.assertEquals(Difficulty.MEDIUM,out.get(0).getDifficulty())
        );

    }

    @Test
    public void test_findById(){
        var e = new Exercise("Mountain Climb", Difficulty.MEDIUM);

        Mockito.doReturn(e).when(exerciseRepository).findById(1);

        var out = exerciseService.findById(1);
        Mockito.verify(exerciseRepository).findById(1);
    }

    @Test
    public void test_save(){
        ExerciseRequest exerciseRequestToSave = new ExerciseRequest("New Exercise", Difficulty.EASY);
        Exercise convertedExerciseToSave = new Exercise(exerciseRequestToSave.getName(), exerciseRequestToSave.getDifficulty());

        Mockito.when(exerciseRepository.save(Mockito.any(Exercise.class))).thenReturn(convertedExerciseToSave);

        ExerciseDTO savedExercise = exerciseService.save(exerciseRequestToSave);

        Mockito.verify(exerciseRepository).save(Mockito.any(Exercise.class));

        Assertions.assertAll(
                () -> Assertions.assertNotNull(savedExercise),
                () -> Assertions.assertEquals("New Exercise", savedExercise.getName()),
                () -> Assertions.assertEquals(Difficulty.EASY, savedExercise.getDifficulty())
        );
    }

    @Test
    public void test_UpdateExercise() {
        ExerciseDTO exerciseDTOToUpdate = new ExerciseDTO(1, "Test", Difficulty.EASY);
        Exercise exerciseEntityToUpdate = ExerciseConverter.fromDTOtoEntity(exerciseDTOToUpdate);

        Mockito.when(exerciseRepository.update(Mockito.any(Exercise.class))).thenReturn(exerciseEntityToUpdate);

        ExerciseDTO updatedExerciseDTO = exerciseService.update(exerciseDTOToUpdate);
        Mockito.verify(exerciseRepository).update(Mockito.any(Exercise.class));

        Assertions.assertAll(
                () -> Assertions.assertNotNull(updatedExerciseDTO),
                () -> Assertions.assertEquals(exerciseDTOToUpdate.getName(), updatedExerciseDTO.getName()),
                () -> Assertions.assertEquals(exerciseDTOToUpdate.getDifficulty(), updatedExerciseDTO.getDifficulty())
        );
    }

    @Test
    public void test_DeleteExercise() {

        ExerciseDTO exerciseDTOToDelete = new ExerciseDTO(1, "Test", Difficulty.EASY);
        Exercise exerciseEntityToDelete = ExerciseConverter.fromDTOtoEntity(exerciseDTOToDelete);

        Mockito.when(exerciseRepository.delete(Mockito.any(Exercise.class))).thenReturn(exerciseEntityToDelete);

        ExerciseDTO deletedExerciseDTO = exerciseService.delete(exerciseDTOToDelete);

        Mockito.verify(exerciseRepository).delete(Mockito.any(Exercise.class));

        Assertions.assertAll(
                () -> Assertions.assertEquals(exerciseDTOToDelete.getId(), deletedExerciseDTO.getId()),
                () -> Assertions.assertEquals(exerciseDTOToDelete.getName(), deletedExerciseDTO.getName()),
                () -> Assertions.assertEquals(exerciseDTOToDelete.getDifficulty(), deletedExerciseDTO.getDifficulty())
        );
    }
}
