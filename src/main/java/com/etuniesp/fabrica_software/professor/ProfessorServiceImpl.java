package com.etuniesp.fabrica_software.professor;

import com.etuniesp.fabrica_software.professor.dto.ProfessorCreateDTO;
import com.etuniesp.fabrica_software.professor.dto.ProfessorResponseDTO;
import com.etuniesp.fabrica_software.professor.dto.ProfessorUpdateDTO;
import com.etuniesp.fabrica_software.stack.StackTecnologia;
import com.etuniesp.fabrica_software.stack.StackTecRepository;
import com.etuniesp.fabrica_software.stack.dto.StackResumo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository repo;
    private final StackTecRepository stackRepo;

    @Override
    public ProfessorResponseDTO criar(ProfessorCreateDTO dto) {
        validarEmail(dto.email());

        Professor entidade = new Professor();
        entidade.setNome(dto.nome());
        entidade.setEmail(dto.email());
        entidade.setTelefone(dto.telefone());
        entidade.setArea(dto.area());
        entidade.setStacks(buscarStacks(dto.stacksIds()));

        Professor salvo = repo.save(entidade);
        return toResponse(salvo);
    }

    @Override
    public ProfessorResponseDTO atualizar(Long id, ProfessorUpdateDTO dto) {
        Professor existente = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado: " + id));

        if (!existente.getEmail().equalsIgnoreCase(dto.email()) && repo.existsByEmail(dto.email())) {
            throw new DataIntegrityViolationException("E-mail já cadastrado.");
        }

        existente.setNome(dto.nome());
        existente.setEmail(dto.email());
        existente.setTelefone(dto.telefone());
        existente.setArea(dto.area());
        existente.setStacks(buscarStacks(dto.stacksIds()));

        return toResponse(existente);
    }

    @Override
    public void excluir(Long id) {
        if (!repo.existsById(id))
            throw new EntityNotFoundException("Professor não encontrado: " + id);
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public ProfessorResponseDTO buscarPorId(Long id) {
        return repo.findById(id).map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProfessorResponseDTO> listar(String termo, Pageable pageable) {
        Page<Professor> page;
        if (termo == null || termo.isBlank()) {
            page = repo.findAll(pageable);
        } else {
            ExampleMatcher matcher = ExampleMatcher.matchingAny()
                    .withIgnoreNullValues()
                    .withMatcher("nome", m -> m.contains().ignoreCase())
                    .withMatcher("email", m -> m.contains().ignoreCase())
                    .withMatcher("area", m -> m.contains().ignoreCase());
            Professor probe = new Professor();
            probe.setNome(termo);
            probe.setEmail(termo);
            probe.setArea(termo);
            page = repo.findAll(Example.of(probe, matcher), pageable);
        }
        return page.map(this::toResponse);
    }

    private void validarEmail(String email) {
        if (repo.existsByEmail(email))
            throw new DataIntegrityViolationException("E-mail já cadastrado.");
    }

    private Set<StackTecnologia> buscarStacks(Set<Long> ids) {
        if (ids == null || ids.isEmpty())
            return Set.of();
        return ids.stream()
                .map(id -> stackRepo.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Stack não encontrada: " + id)))
                .collect(Collectors.toSet());
    }

    private ProfessorResponseDTO toResponse(Professor p) {
        return new ProfessorResponseDTO(
                p.getId(),
                p.getNome(),
                p.getEmail(),
                p.getTelefone(),
                p.getArea(),
                p.getStacks().stream()
                        .map(s -> new StackResumo(s.getId(), s.getNome(), s.getCategoria()))
                        .collect(Collectors.toSet())
        );
    }
}


