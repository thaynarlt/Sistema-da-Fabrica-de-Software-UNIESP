package com.etuniesp.fabrica_software.projeto.dto;

import com.etuniesp.fabrica_software.projeto.enums.StatusProjeto;
import com.etuniesp.fabrica_software.projeto.enums.TipoProjeto;

import java.time.LocalDate;
import java.util.Set;

/**
 * DTO de resposta para Projeto.
 * 
 * Os campos professorId e professorNome: removidos da entrega,
 * solicitado pelo professor. A entrega deve conter apenas os relacionamentos
 * com Aluno e Stack.
 */
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
        // Campos professorId e professorNome removidos - não solicitados para a entrega
        Set<Long> stacksIds,
        Set<Long> alunosIds
) {}


