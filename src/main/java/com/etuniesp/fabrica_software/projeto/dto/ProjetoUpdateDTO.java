package com.etuniesp.fabrica_software.projeto.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.Set;

public record ProjetoUpdateDTO(
        @NotBlank String titulo,
        String descricao,
        LocalDate dataInicio,
        LocalDate dataFim,
        Long professorId,
        Set<Long> alunosIds
) {}


