package com.gym.domain.mapper;

import com.gym.domain.dto.InstructorDTO;
import com.gym.domain.dto.InstructorRequest;
import com.gym.domain.entity.Instructor;

public class InstructorConverter {

    public static InstructorDTO toDTO(Instructor instructor){
        InstructorDTO instructorDTO = new InstructorDTO();
        instructorDTO.setId(instructor.getId());
        instructorDTO.setName(instructor.getName());
        return  instructorDTO;
    }

    public static Instructor toEntity(InstructorRequest instructorRequest){
        Instructor instructor = new Instructor();
        instructor.setName(instructorRequest.getName());
        return instructor;
    }

    public static Instructor fromDTOtoEntity(InstructorDTO instructorDTO){
        Instructor instructor = new Instructor();
        instructor.setId(instructorDTO.getId());
        instructor.setName(instructorDTO.getName());
        return instructor;
    }
}
