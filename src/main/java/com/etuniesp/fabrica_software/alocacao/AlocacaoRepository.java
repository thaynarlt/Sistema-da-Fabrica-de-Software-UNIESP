package com.etuniesp.fabrica_software.alocacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlocacaoRepository extends JpaRepository<Alocacao, Long> {

    List<Alocacao> findByAlunoId(Long alunoId);

    List<Alocacao> findByProjetoId(Long projetoId);

    @Query("SELECT a FROM Alocacao a WHERE a.aluno.id = :alunoId AND a.status = 'ATIVA'")
    List<Alocacao> findAtivasByAlunoId(@Param("alunoId") Long alunoId);

    @Query("SELECT a FROM Alocacao a WHERE a.projeto.id = :projetoId AND a.status = 'ATIVA'")
    List<Alocacao> findAtivasByProjetoId(@Param("projetoId") Long projetoId);

    Optional<Alocacao> findByAlunoIdAndProjetoId(Long alunoId, Long projetoId);

    @Query("SELECT a FROM Alocacao a WHERE a.aluno.id = :alunoId AND a.projeto.id = :projetoId AND a.status = 'ATIVA'")
    Optional<Alocacao> findAtivaByAlunoIdAndProjetoId(@Param("alunoId") Long alunoId, @Param("projetoId") Long projetoId);
}

