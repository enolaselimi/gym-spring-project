package com.gym.service;

import com.gym.domain.dto.ExerciseDTO;
import com.gym.domain.dto.ExerciseRequest;
import com.gym.domain.dto.InstructorDTO;
import com.gym.domain.dto.InstructorRequest;
import com.gym.domain.entity.Difficulty;
import com.gym.domain.entity.Exercise;
import com.gym.domain.entity.Instructor;
import com.gym.domain.mapper.ExerciseConverter;
import com.gym.domain.mapper.InstructorConverter;
import com.gym.repository.InstructorRepository;
import com.gym.service.impl.InstructorServiceImpl;
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
public class InstructorServiceMockitoTest {
    @InjectMocks
    private InstructorService instructorService = Mockito.spy(new InstructorServiceImpl());

    @Mock
    private InstructorRepository instructorRepository;

    @Test
    public void test_findAllInstructors(){
        List<Instructor> instructorList = new ArrayList<>();
        var i = new Instructor("Test");
        instructorList.add(i);
        Mockito.doReturn(instructorList).when(instructorRepository).findAll();


        var out = instructorService.findAll();

        Mockito.verify(instructorRepository).findAll();
        Assertions.assertEquals("Test",out.get(0).getName());
    }

    @Test
    public void test_findById(){
        var i = new Instructor("Test");

        Mockito.doReturn(i).when(instructorRepository).findById(1);

        var out = instructorService.findById(1);
        Mockito.verify(instructorRepository).findById(1);
    }

    @Test
    public void test_saveInstructor(){
        InstructorRequest instructorRequestToSave = new InstructorRequest("New Instructor");
        Instructor convertedInstructorToSave = new Instructor(instructorRequestToSave.getName());

        Mockito.when(instructorRepository.save(Mockito.any(Instructor.class))).thenReturn(convertedInstructorToSave);

        InstructorDTO savedInstructor = instructorService.save(instructorRequestToSave);

        Mockito.verify(instructorRepository).save(Mockito.any(Instructor.class));

        Assertions.assertAll(
                () -> Assertions.assertNotNull(savedInstructor),
                () -> Assertions.assertEquals("New Instructor", savedInstructor.getName())
        );
    }

    @Test
    public void test_UpdateInstructor() {
        InstructorDTO instructorDTOToUpdate = new InstructorDTO(1, "Test");
        Instructor instructorEntityToUpdate = InstructorConverter.fromDTOtoEntity(instructorDTOToUpdate);

        Mockito.when(instructorRepository.update(Mockito.any(Instructor.class))).thenReturn(instructorEntityToUpdate);

        InstructorDTO updatedInstructorDTO = instructorService.update(instructorDTOToUpdate);
        Mockito.verify(instructorRepository).update(Mockito.any(Instructor.class));

        Assertions.assertAll(
                () -> Assertions.assertNotNull(updatedInstructorDTO),
                () -> Assertions.assertEquals(instructorDTOToUpdate.getName(), updatedInstructorDTO.getName())
        );
    }

    @Test
    public void test_DeleteInstructor() {

        InstructorDTO instructorDTOToDelete = new InstructorDTO(1, "Test");
        Instructor instructorEntityToDelete = InstructorConverter.fromDTOtoEntity(instructorDTOToDelete);

        Mockito.when(instructorRepository.delete(Mockito.any(Instructor.class))).thenReturn(instructorEntityToDelete);

        InstructorDTO deletedInstructorDTO = instructorService.delete(instructorDTOToDelete);

        Mockito.verify(instructorRepository).delete(Mockito.any(Instructor.class));

        Assertions.assertAll(
                () -> Assertions.assertEquals(instructorDTOToDelete.getId(), deletedInstructorDTO.getId()),
                () -> Assertions.assertEquals(instructorDTOToDelete.getName(), deletedInstructorDTO.getName())
        );
    }
}
