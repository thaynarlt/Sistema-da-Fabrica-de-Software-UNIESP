package com.etuniesp.fabrica_software.certificado;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificadoParticipacaoRepository extends JpaRepository<CertificadoParticipacao, Long> {

    /**
     * Busca certificado por código de verificação (para verificação pública)
     */
    Optional<CertificadoParticipacao> findByCodigoVerificacao(String codigoVerificacao);

    /**
     * Lista todos os certificados de um aluno
     */
    List<CertificadoParticipacao> findByAlunoId(Long alunoId);

    /**
     * Lista todos os certificados de um projeto
     */
    List<CertificadoParticipacao> findByProjetoId(Long projetoId);

    /**
     * Lista certificados ativos de um aluno
     */
    @Query("SELECT c FROM CertificadoParticipacao c WHERE c.aluno.id = :alunoId AND c.ativo = true")
    List<CertificadoParticipacao> findAtivosByAlunoId(@Param("alunoId") Long alunoId);

    /**
     * Lista certificados ativos de um projeto
     */
    @Query("SELECT c FROM CertificadoParticipacao c WHERE c.projeto.id = :projetoId AND c.ativo = true")
    List<CertificadoParticipacao> findAtivosByProjetoId(@Param("projetoId") Long projetoId);

    /**
     * Busca certificado por alocação
     */
    List<CertificadoParticipacao> findByAlocacaoId(Long alocacaoId);

    /**
     * Busca o certificado ativo mais recente de uma alocação
     */
    @Query("SELECT c FROM CertificadoParticipacao c WHERE c.alocacao.id = :alocacaoId AND c.ativo = true ORDER BY c.versao DESC")
    Optional<CertificadoParticipacao> findAtivoByAlocacaoId(@Param("alocacaoId") Long alocacaoId);
}

