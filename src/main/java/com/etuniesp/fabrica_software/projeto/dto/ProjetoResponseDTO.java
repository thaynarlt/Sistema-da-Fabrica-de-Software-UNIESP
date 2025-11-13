package com.etuniesp.fabrica_software.projeto.dto;

import com.etuniesp.fabrica_software.projeto.enums.StatusProjeto;
import com.etuniesp.fabrica_software.projeto.enums.TipoProjeto;

import java.time.LocalDate;
import java.util.Set;

public record ProjetoResponseDTO(
        Long id,
        String titulo,
        String descricao,
        String semestre,
        TipoProjeto tipo,
        String empresaParceira,
        StatusProjeto status,
        LocalDate dataInicio,
        LocalDate dataFim,
        Long professorId,
        String professorNome,
        Set<Long> stacksIds,
        Set<Long> alunosIds
) {}


