package com.etuniesp.fabrica_software.certificado.dto;

/**
 * DTO de resposta para Certificado de Participação.
 * 
 * Não inclui campos de Professor (professorId, professorNome),
 * conforme solicitado. O certificado exibe apenas informações de
 * Aluno, Projeto e Alocacao.
 */
public record CertificadoParticipacaoResponseDTO(
        Long id,
        // Dados do Aluno
        Long alunoId,
        String alunoNome,
        // Dados do Projeto
        Long projetoId,
        String projetoTitulo,
        String semestre,
        // Dados da Alocacao
        Long alocacaoId,
        String papel,
        Integer cargaHoraria,
        // Dados do Certificado
        String codigoVerificacao,
        Integer versao,
        Boolean ativo,
        String pdfUrl
) {}

