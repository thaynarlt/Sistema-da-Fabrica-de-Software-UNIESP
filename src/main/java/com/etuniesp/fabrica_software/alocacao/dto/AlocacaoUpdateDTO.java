package com.etuniesp.fabrica_software.alocacao.dto;

import com.etuniesp.fabrica_software.alocacao.enums.PapelAlocacao;
import com.etuniesp.fabrica_software.alocacao.enums.StatusAlocacao;

import java.time.LocalDate;

public record AlocacaoUpdateDTO(
        PapelAlocacao papel,
        LocalDate dataInicio,
        LocalDate dataFim,
        Integer cargaHorariaPrevista,
        Integer cargaHorariaRealizada,
        StatusAlocacao status,
        String observacoes
) {}

