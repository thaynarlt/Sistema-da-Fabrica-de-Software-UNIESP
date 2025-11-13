package com.etuniesp.fabrica_software.alocacao;

import com.etuniesp.fabrica_software.alocacao.dto.AlocacaoCreateDTO;
import com.etuniesp.fabrica_software.alocacao.dto.AlocacaoResponseDTO;
import com.etuniesp.fabrica_software.alocacao.dto.AlocacaoUpdateDTO;
import com.etuniesp.fabrica_software.alocacao.enums.StatusAlocacao;
import com.etuniesp.fabrica_software.aluno.Aluno;
import com.etuniesp.fabrica_software.aluno.AlunoRepository;
import com.etuniesp.fabrica_software.projeto.Projeto;
import com.etuniesp.fabrica_software.projeto.ProjetoRepository;
import com.etuniesp.fabrica_software.projeto.enums.StatusProjeto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AlocacaoServiceImpl implements AlocacaoService {

    private final AlocacaoRepository repo;
    private final AlunoRepository alunoRepo;
    private final ProjetoRepository projetoRepo;

    @Override
    public AlocacaoResponseDTO criar(AlocacaoCreateDTO dto) {
        // Validar se aluno existe
        Aluno aluno = alunoRepo.findById(dto.alunoId())
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado: " + dto.alunoId()));

        // Validar se projeto existe
        Projeto projeto = projetoRepo.findById(dto.projetoId())
                .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado: " + dto.projetoId()));

        // Validar se projeto está em status válido para alocação
        if (projeto.getStatus() != StatusProjeto.PLANEJADO && projeto.getStatus() != StatusProjeto.EM_ANDAMENTO) {
            throw new IllegalArgumentException("Projeto deve estar PLANEJADO ou EM_ANDAMENTO para alocar alunos");
        }

        // Verificar se já existe alocação ativa para este aluno e projeto
        repo.findAtivaByAlunoIdAndProjetoId(dto.alunoId(), dto.projetoId())
                .ifPresent(a -> {
                    throw new IllegalArgumentException("Aluno já está alocado ativamente neste projeto");
                });

        // Criar alocação
        Alocacao alocacao = Alocacao.builder()
                .aluno(aluno)
                .projeto(projeto)
                .papel(dto.papel())
                .dataInicio(dto.dataInicio())
                .dataFim(dto.dataFim())
                .cargaHorariaPrevista(dto.cargaHorariaPrevista())
                .cargaHorariaRealizada(dto.cargaHorariaRealizada())
                .status(StatusAlocacao.ATIVA)
                .observacoes(dto.observacoes())
                .build();

        Alocacao salva = repo.save(alocacao);
        return toResponse(salva);
    }

    @Override
    public AlocacaoResponseDTO atualizar(Long id, AlocacaoUpdateDTO dto) {
        Alocacao existente = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Alocação não encontrada: " + id));

        if (dto.papel() != null) {
            existente.setPapel(dto.papel());
        }
        if (dto.dataInicio() != null) {
            existente.setDataInicio(dto.dataInicio());
        }
        if (dto.dataFim() != null) {
            existente.setDataFim(dto.dataFim());
        }
        if (dto.cargaHorariaPrevista() != null) {
            existente.setCargaHorariaPrevista(dto.cargaHorariaPrevista());
        }
        if (dto.cargaHorariaRealizada() != null) {
            existente.setCargaHorariaRealizada(dto.cargaHorariaRealizada());
        }
        if (dto.status() != null) {
            existente.setStatus(dto.status());
        }
        if (dto.observacoes() != null) {
            existente.setObservacoes(dto.observacoes());
        }

        return toResponse(existente);
    }

    @Override
    public void excluir(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Alocação não encontrada: " + id);
        }
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public AlocacaoResponseDTO buscarPorId(Long id) {
        return repo.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Alocação não encontrada: " + id));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AlocacaoResponseDTO> listar(String termo, Pageable pageable) {
        Page<Alocacao> page;
        if (termo == null || termo.isBlank()) {
            page = repo.findAll(pageable);
        } else {
            // Busca por nome do aluno ou título do projeto
            page = repo.findAll(pageable); // Simplificado - pode melhorar com query customizada
        }
        return page.map(this::toResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AlocacaoResponseDTO> listarPorAluno(Long alunoId) {
        return repo.findByAlunoId(alunoId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<AlocacaoResponseDTO> listarPorProjeto(Long projetoId) {
        return repo.findByProjetoId(projetoId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AlocacaoResponseDTO encerrar(Long id, Integer cargaHorariaRealizada, String observacoes) {
        Alocacao alocacao = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Alocação não encontrada: " + id));

        if (alocacao.getStatus() == StatusAlocacao.CONCLUIDA) {
            throw new IllegalArgumentException("Alocação já está concluída");
        }

        alocacao.setStatus(StatusAlocacao.CONCLUIDA);
        alocacao.setDataFim(java.time.LocalDate.now());
        
        if (cargaHorariaRealizada != null) {
            alocacao.setCargaHorariaRealizada(cargaHorariaRealizada);
        }
        
        if (observacoes != null && !observacoes.isBlank()) {
            alocacao.setObservacoes(observacoes);
        }

        return toResponse(alocacao);
    }

    private AlocacaoResponseDTO toResponse(Alocacao a) {
        return new AlocacaoResponseDTO(
                a.getId(),
                a.getAluno().getId(),
                a.getAluno().getNome(),
                a.getProjeto().getId(),
                a.getProjeto().getTitulo(),
                a.getPapel(),
                a.getDataInicio(),
                a.getDataFim(),
                a.getCargaHorariaPrevista(),
                a.getCargaHorariaRealizada(),
                a.getStatus(),
                a.getObservacoes()
        );
    }
}

