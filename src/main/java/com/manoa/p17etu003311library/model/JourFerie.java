package com.manoa.p17etu003311library.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "jour_ferie")
public class JourFerie {
    @Id
    private LocalDate date;

    @Column(nullable = false)
    private String description;
}