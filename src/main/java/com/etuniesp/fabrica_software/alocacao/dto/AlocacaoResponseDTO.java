package com.etuniesp.fabrica_software.alocacao.dto;

import com.etuniesp.fabrica_software.alocacao.enums.PapelAlocacao;
import com.etuniesp.fabrica_software.alocacao.enums.StatusAlocacao;

import java.time.LocalDate;

public record AlocacaoResponseDTO(
        Long id,
        Long alunoId,
        String alunoNome,
        Long projetoId,
        String projetoTitulo,
        PapelAlocacao papel,
        LocalDate dataInicio,
        LocalDate dataFim,
        Integer cargaHorariaPrevista,
        Integer cargaHorariaRealizada,
        StatusAlocacao status,
        String observacoes
) {}

