package com.gym.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gym.domain.entity.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanDTO {
    @JsonIgnore
    private Integer id;
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @NotNull(message = "Instructor cannot be blank")
    private InstructorDTO instructorDTO;
    @NotNull(message = "Difficulty cannot be blank")
    private Difficulty difficulty;
}
