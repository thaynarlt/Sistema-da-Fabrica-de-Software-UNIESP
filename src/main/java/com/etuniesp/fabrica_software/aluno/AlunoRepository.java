package com.etuniesp.fabrica_software.aluno;

import com.etuniesp.fabrica_software.aluno.enums.Periodo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    boolean existsByEmail(String email);

    boolean existsByMatricula(String matricula);

    Optional<Aluno> findByEmail(String email);

    // Spring Data
    Page<Aluno> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    Page<Aluno> findByNomeContainingIgnoreCaseAndPeriodoIn(
            String nome, Collection<Periodo> periodos, Pageable pageable);

    Page<Aluno> findDistinctByStacks_NomeInIgnoreCase(Collection<String> nomesStacks, Pageable pageable);

    // JPQL com @Query
    // @Query("""
    // select distinct a
    // from Aluno a
    // join a.stacks s
    // where lower(s.nome) in :stacks
    // and (:nome is null or lower(a.nome) like lower(concat('%', :nome, '%')))
    // """)
    // Page<Aluno> buscarPorStacksENome(
    // @Param("stacks") Collection<String> stacks,
    // @Param("nome") String nome,
    // Pageable pageable);

    // Projeção DTO (record) — performático e limpo no controller
    // public record AlunoListaDTO(Long id, String nome, String email, String
    // periodo, int qtdCertificados) {}
    //
    // @Query("""
    // select new br.edu.uniesp.softfact.aluno.dto.AlunoListaDTO(
    // a.id, a.nome, a.email, cast(a.periodo as string), count(c.id)
    // )
    // from Aluno a
    // left join a.certificados c
    // where (:q is null or lower(a.nome) like lower(concat('%', :q, '%'))
    // or lower(a.email) like lower(concat('%', :q, '%')))
    // group by a.id, a.nome, a.email, a.periodo
    // """)
    // Page<AlunoListaDTO> listarResumo(@Param("q") String q, Pageable pageable);

    // Quando a tela precisa carregar Aluno + Stacks numa tacada só:
    // @Query("""
    // select distinct a
    // from Aluno a
    // left join fetch a.stacks s
    // where (:id is null or a.id = :id)
    // """)
    // List<Aluno> carregarComStacks(@Param("id") Long id);
}
