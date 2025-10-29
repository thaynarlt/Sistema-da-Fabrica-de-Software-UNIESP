package com.etuniesp.fabrica_software.aluno;

import com.etuniesp.fabrica_software.aluno.dto.AlunoCreateDTO;
import com.etuniesp.fabrica_software.aluno.dto.AlunoResponseDTO;
import com.etuniesp.fabrica_software.aluno.dto.AlunoUpdateDTO;
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
public class AlunoServiceImpl implements AlunoService {

    private final AlunoRepository repo;
    private final StackTecRepository stackRepo;

    @Override
    public AlunoResponseDTO criar(AlunoCreateDTO dto) {
        validarUnicidades(dto.email(), dto.matricula());

        Aluno entidade = toEntity(new Aluno(), dto.stacksIds());
        entidade.setNome(dto.nome());
        entidade.setEmail(dto.email());
        entidade.setTelefone(dto.telefone());
        entidade.setCurso(dto.curso());
        entidade.setMatricula(dto.matricula());
        entidade.setPeriodo(dto.periodo());

        Aluno salvo = repo.save(entidade);
        return toResponse(salvo);
    }

    @Override
    public AlunoResponseDTO atualizar(Long id, AlunoUpdateDTO dto) {
        Aluno existente = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado: " + id));

        // se trocar email/matricula, garantir unicidade
        if (!existente.getEmail().equalsIgnoreCase(dto.email()) && repo.existsByEmail(dto.email())) {
            throw new DataIntegrityViolationException("E-mail já cadastrado.");
        }
        if (!existente.getMatricula().equalsIgnoreCase(dto.matricula()) && repo.existsByMatricula(dto.matricula())) {
            throw new DataIntegrityViolationException("Matrícula já cadastrada.");
        }

        existente.setNome(dto.nome());
        existente.setEmail(dto.email());
        existente.setTelefone(dto.telefone());
        existente.setCurso(dto.curso());
        existente.setMatricula(dto.matricula());
        existente.setPeriodo(dto.periodo());
        existente.setStacks(buscarStacks(dto.stacksIds()));

        return toResponse(existente);
    }

    @Override
    public void excluir(Long id) {
        if (!repo.existsById(id))
            throw new EntityNotFoundException("Aluno não encontrado: " + id);
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public AlunoResponseDTO buscarPorId(Long id) {
        return repo.findById(id).map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AlunoResponseDTO> listar(String termo, Pageable pageable) {
        Page<Aluno> page;
        if (termo == null || termo.isBlank()) {
            page = repo.findAll(pageable);
        } else {
            // busca simples por nome/email/matrícula (exemplo com ExampleMatcher)
            ExampleMatcher matcher = ExampleMatcher.matchingAny()
                    .withIgnoreNullValues()
                    .withMatcher("nome", m -> m.contains().ignoreCase())
                    .withMatcher("email", m -> m.contains().ignoreCase())
                    .withMatcher("matricula", m -> m.contains().ignoreCase());
            Aluno probe = new Aluno();
            probe.setNome(termo);
            probe.setEmail(termo);
            probe.setMatricula(termo);
            page = repo.findAll(Example.of(probe, matcher), pageable);
        }
        return page.map(this::toResponse);
    }

    private void validarUnicidades(String email, String matricula) {
        if (repo.existsByEmail(email))
            throw new DataIntegrityViolationException("E-mail já cadastrado.");
        if (repo.existsByMatricula(matricula))
            throw new DataIntegrityViolationException("Matrícula já cadastrada.");
    }

    private Set<StackTecnologia> buscarStacks(Set<Long> ids) {
        if (ids == null || ids.isEmpty())
            return Set.of();
        return ids.stream()
                .map(id -> stackRepo.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Stack não encontrada: " + id)))
                .collect(Collectors.toSet());
    }

    private Aluno toEntity(Aluno a, Set<Long> stacksIds) {
        a.setStacks(buscarStacks(stacksIds));
        return a;
    }

    private AlunoResponseDTO toResponse(Aluno a) {
        return new AlunoResponseDTO(
                a.getId(),
                a.getNome(),
                a.getEmail(),
                a.getTelefone(),
                a.getCurso(),
                a.getMatricula(),
                a.getPeriodo(),
                a.getStacks().stream()
                        .map(s -> new StackResumo(s.getId(), s.getNome(), s.getCategoria()))
                        .collect(Collectors.toSet()));
    }
}
