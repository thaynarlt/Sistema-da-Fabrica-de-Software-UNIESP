package com.etuniesp.fabrica_software.projeto;

import com.etuniesp.fabrica_software.aluno.Aluno;
import com.etuniesp.fabrica_software.aluno.AlunoRepository;
// import com.etuniesp.fabrica_software.professor.Professor; // Comentado - não utilizado na entrega
// import com.etuniesp.fabrica_software.professor.ProfessorRepository; // Comentado - não utilizado na entrega
import com.etuniesp.fabrica_software.projeto.dto.*;
import com.etuniesp.fabrica_software.stack.StackTecnologia;
import com.etuniesp.fabrica_software.stack.StackTecRepository;
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
    // private final ProfessorRepository professorRepo; // Comentado - relacionamento com Professor removido da entrega
    private final AlunoRepository alunoRepo;
    private final StackTecRepository stackRepo;

    @Override
    public ProjetoResponseDTO criar(ProjetoCreateDTO dto) {
        Projeto entidade = new Projeto();
        aplicarDTO(entidade, dto.titulo(), dto.descricao(), dto.semestre(), dto.tipo(), dto.empresaParceira(), 
                   dto.status(), dto.dataInicio(), dto.dataFim(), 
                   /* dto.professorId() - REMOVIDO: não solicitado para a entrega */ null,
                   dto.stacksIds(), dto.alunosIds());
        Projeto salvo = repo.save(entidade);
        return toResponse(salvo);
    }

    @Override
    public ProjetoResponseDTO atualizar(Long id, ProjetoUpdateDTO dto) {
        Projeto existente = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado: " + id));
        aplicarDTO(existente, dto.titulo(), dto.descricao(), dto.semestre(), dto.tipo(), dto.empresaParceira(), 
                   dto.status(), dto.dataInicio(), dto.dataFim(), 
                   /* dto.professorId() - REMOVIDO: não solicitado para a entrega */ null,
                   dto.stacksIds(), dto.alunosIds());
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

    private void aplicarDTO(Projeto p, String titulo, String descricao, String semestre, 
                             com.etuniesp.fabrica_software.projeto.enums.TipoProjeto tipo, 
                             String empresaParceira,
                             com.etuniesp.fabrica_software.projeto.enums.StatusProjeto status,
                             java.time.LocalDate di, java.time.LocalDate df,
                             Long professorId, Set<Long> stacksIds, Set<Long> alunosIds) {
        p.setTitulo(titulo);
        p.setDescricao(descricao);
        p.setSemestre(semestre);
        p.setTipo(tipo != null ? tipo : com.etuniesp.fabrica_software.projeto.enums.TipoProjeto.AUTORAL);
        p.setEmpresaParceira(empresaParceira);
        p.setStatus(status != null ? status : com.etuniesp.fabrica_software.projeto.enums.StatusProjeto.PLANEJADO);
        p.setDataInicio(di);
        p.setDataFim(df);
        
        /*
         * RELACIONAMENTO COM PROFESSOR REMOVIDO PARA A ENTREGA
         * 
         * Motivo: O professor solicitou que a entrega do CRUD de Projeto contenha
         * apenas os relacionamentos com Aluno e Stack. O código abaixo foi comentado
         * mas mantido para referência futura.
         */
        // if (professorId != null) {
        //     Professor prof = professorRepo.findById(professorId)
        //             .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado: " + professorId));
        //     p.setProfessor(prof);
        // } else {
        //     p.setProfessor(null);
        // }
        
        // Gerenciar stacks desejadas
        if (stacksIds == null || stacksIds.isEmpty()) {
            p.getStacksDesejadas().clear();
        } else {
            Set<StackTecnologia> stacks = stacksIds.stream()
                    .map(sid -> stackRepo.findById(sid)
                            .orElseThrow(() -> new EntityNotFoundException("Stack não encontrada: " + sid)))
                    .collect(Collectors.toSet());
            p.setStacksDesejadas(stacks);
        }
        
        // Gerenciar alunos
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
        /*
         * RELACIONAMENTO COM PROFESSOR REMOVIDO DO RESPONSE
         * 
         * Motivo: O professor solicitou que a entrega contenha apenas os relacionamentos
         * com Aluno e Stack. Os campos professorId e professorNome foram removidos do DTO.
         */
        // Long profId = p.getProfessor() != null ? p.getProfessor().getId() : null;
        // String profNome = p.getProfessor() != null ? p.getProfessor().getNome() : null;
        
        Set<Long> stacksIds = p.getStacksDesejadas() != null ? 
                p.getStacksDesejadas().stream().map(StackTecnologia::getId).collect(Collectors.toSet()) : Set.of();
        Set<Long> alunosIds = p.getAlunos() != null ? 
                p.getAlunos().stream().map(Aluno::getId).collect(Collectors.toSet()) : Set.of();
        return new ProjetoResponseDTO(
                p.getId(),
                p.getTitulo(),
                p.getDescricao(),
                p.getSemestre(),
                p.getTipo(),
                p.getEmpresaParceira(),
                p.getStatus(),
                p.getDataInicio(),
                p.getDataFim(),
                // profId, // REMOVIDO: não solicitado para a entrega
                // profNome, // REMOVIDO: não solicitado para a entrega
                stacksIds,
                alunosIds
        );
    }
}


