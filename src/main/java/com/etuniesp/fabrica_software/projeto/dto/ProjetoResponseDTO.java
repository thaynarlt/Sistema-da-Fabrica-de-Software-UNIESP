package com.etuniesp.fabrica_software.projeto.dto;

import java.time.LocalDate;
import java.util.Set;

public record ProjetoResponseDTO(
        Long id,
        String titulo,
        String descricao,
        LocalDate dataInicio,
        LocalDate dataFim,
        Long professorId,
        String professorNome,
        Set<Long> alunosIds
) {}


