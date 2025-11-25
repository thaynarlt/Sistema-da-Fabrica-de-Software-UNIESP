package com.etuniesp.fabrica_software.certificado;

import com.etuniesp.fabrica_software.certificado.dto.CertificadoParticipacaoCreateDTO;
import com.etuniesp.fabrica_software.certificado.dto.CertificadoParticipacaoResponseDTO;

import java.util.List;

public interface CertificadoParticipacaoService {

    /**
     * Emite um certificado de participação para uma alocação.
     * Valida se a alocação está concluída e tem carga horária.
     */
    CertificadoParticipacaoResponseDTO emitir(CertificadoParticipacaoCreateDTO dto);

    /**
     * Busca certificado por ID
     */
    CertificadoParticipacaoResponseDTO buscarPorId(Long id);

    /**
     * Busca certificado por código de verificação (para verificação pública)
     */
    CertificadoParticipacaoResponseDTO buscarPorCodigoVerificacao(String codigoVerificacao);

    /**
     * Lista todos os certificados de um aluno
     */
    List<CertificadoParticipacaoResponseDTO> listarPorAluno(Long alunoId);

    /**
     * Lista todos os certificados de um projeto
     */
    List<CertificadoParticipacaoResponseDTO> listarPorProjeto(Long projetoId);

    /**
     * Lista apenas certificados ativos de um aluno
     */
    List<CertificadoParticipacaoResponseDTO> listarAtivosPorAluno(Long alunoId);

    /**
     * Republica um certificado (cria nova versão e desativa a anterior)
     */
    CertificadoParticipacaoResponseDTO republicar(Long id);

    /**
     * Desativa um certificado (marca como inativo)
     */
    void desativar(Long id);

    /**
     * Gera ou regenera o PDF de um certificado existente
     */
    CertificadoParticipacaoResponseDTO gerarPdf(Long id);
}

