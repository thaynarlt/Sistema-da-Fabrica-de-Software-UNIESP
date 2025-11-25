package com.etuniesp.fabrica_software.projeto.dto;

import com.etuniesp.fabrica_software.projeto.enums.StatusProjeto;
import com.etuniesp.fabrica_software.projeto.enums.TipoProjeto;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.Set;

public record ProjetoCreateDTO(
        @NotBlank String titulo,
        String descricao,
        @NotBlank String semestre,
        TipoProjeto tipo,
        String empresaParceira,
        StatusProjeto status,
        LocalDate dataInicio,
        LocalDate dataFim,
        // Long professorId, // REMOVIDO: Relacionamento com Professor não foi solicitado para entrega
        Set<Long> stacksIds,
        Set<Long> alunosIds
) {}


