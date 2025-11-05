package com.etuniesp.fabrica_software.projeto;

import com.etuniesp.fabrica_software.aluno.Aluno;
import com.etuniesp.fabrica_software.aluno.AlunoRepository;
import com.etuniesp.fabrica_software.professor.Professor;
import com.etuniesp.fabrica_software.professor.ProfessorRepository;
import com.etuniesp.fabrica_software.projeto.dto.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjetoServiceImpl implements ProjetoService {

    private final ProjetoRepository repo;
    private final ProfessorRepository professorRepo;
    private final AlunoRepository alunoRepo;

    @Override
    public ProjetoResponseDTO criar(ProjetoCreateDTO dto) {
        Projeto entidade = new Projeto();
        aplicarDTO(entidade, dto.titulo(), dto.descricao(), dto.dataInicio(), dto.dataFim(), dto.professorId(), dto.alunosIds());
        Projeto salvo = repo.save(entidade);
        return toResponse(salvo);
    }

    @Override
    public ProjetoResponseDTO atualizar(Long id, ProjetoUpdateDTO dto) {
        Projeto existente = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado: " + id));
        aplicarDTO(existente, dto.titulo(), dto.descricao(), dto.dataInicio(), dto.dataFim(), dto.professorId(), dto.alunosIds());
        return toResponse(existente);
    }

    @Override
    public void excluir(Long id) {
        if (!repo.existsById(id))
            throw new EntityNotFoundException("Projeto não encontrado: " + id);
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public ProjetoResponseDTO buscarPorId(Long id) {
        return repo.findById(id).map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProjetoResponseDTO> listar(String termo, Pageable pageable) {
        Page<Projeto> page;
        if (termo == null || termo.isBlank()) {
            page = repo.findAll(pageable);
        } else {
            ExampleMatcher matcher = ExampleMatcher.matchingAny()
                    .withIgnoreNullValues()
                    .withMatcher("titulo", m -> m.contains().ignoreCase())
                    .withMatcher("descricao", m -> m.contains().ignoreCase());
            Projeto probe = new Projeto();
            probe.setTitulo(termo);
            probe.setDescricao(termo);
            page = repo.findAll(Example.of(probe, matcher), pageable);
        }
        return page.map(this::toResponse);
    }

    private void aplicarDTO(Projeto p, String titulo, String descricao, java.time.LocalDate di, java.time.LocalDate df,
                             Long professorId, Set<Long> alunosIds) {
        p.setTitulo(titulo);
        p.setDescricao(descricao);
        p.setDataInicio(di);
        p.setDataFim(df);
        if (professorId != null) {
            Professor prof = professorRepo.findById(professorId)
                    .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado: " + professorId));
            p.setProfessor(prof);
        } else {
            p.setProfessor(null);
        }
        if (alunosIds == null || alunosIds.isEmpty()) {
            p.getAlunos().clear();
        } else {
            Set<Aluno> alunos = alunosIds.stream()
                    .map(aid -> alunoRepo.findById(aid)
                            .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado: " + aid)))
                    .collect(Collectors.toSet());
            p.setAlunos(alunos);
        }
    }

    private ProjetoResponseDTO toResponse(Projeto p) {
        Long profId = p.getProfessor() != null ? p.getProfessor().getId() : null;
        String profNome = p.getProfessor() != null ? p.getProfessor().getNome() : null;
        Set<Long> alunosIds = p.getAlunos() != null ? p.getAlunos().stream().map(Aluno::getId).collect(Collectors.toSet()) : Set.of();
        return new ProjetoResponseDTO(
                p.getId(),
                p.getTitulo(),
                p.getDescricao(),
                p.getDataInicio(),
                p.getDataFim(),
                profId,
                profNome,
                alunosIds
        );
    }
}


