package com.gym.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client extends BaseEntity{
    private String name;
    private String email;
    private LocalDate dateOfBirth;
    private LocalDate dateJoined;
    private Float weight;
    private Float height;
    @ManyToOne
    @JoinColumn(name = "plan_id", referencedColumnName = "id")
    private Plan plan;
}
