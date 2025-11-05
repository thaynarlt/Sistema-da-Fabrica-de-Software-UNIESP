package com.etuniesp.fabrica_software.professor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record ProfessorUpdateDTO(
        @NotBlank String nome,
        @Email String email,
        String telefone,
        String area,
        Set<Long> stacksIds
) {}


