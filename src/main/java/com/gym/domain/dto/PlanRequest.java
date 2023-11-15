package com.gym.domain.dto;

import com.gym.domain.entity.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlanRequest {
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @NotNull(message = "Instructor cannot be blank")
    @Positive(message = "ID must be a positive integer")
    private Integer instructor_id;
    private Difficulty difficulty;
}
