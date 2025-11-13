package com.etuniesp.fabrica_software.alocacao.dto;

import com.etuniesp.fabrica_software.alocacao.enums.PapelAlocacao;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AlocacaoCreateDTO(
        @NotNull Long alunoId,
        @NotNull Long projetoId,
        @NotNull PapelAlocacao papel,
        @NotNull LocalDate dataInicio,
        LocalDate dataFim,
        @NotNull Integer cargaHorariaPrevista,
        Integer cargaHorariaRealizada,
        String observacoes
) {}

