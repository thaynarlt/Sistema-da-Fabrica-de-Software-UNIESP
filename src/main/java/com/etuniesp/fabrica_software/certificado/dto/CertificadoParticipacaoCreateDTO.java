package com.etuniesp.fabrica_software.certificado.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO para criação de Certificado de Participação.
 * 
 * Não inclui campos de Professor, conforme solicitado.
 * O certificado é baseado apenas em Aluno, Projeto e Alocacao.
 */
public record CertificadoParticipacaoCreateDTO(
        @NotNull Long alocacaoId
) {}

