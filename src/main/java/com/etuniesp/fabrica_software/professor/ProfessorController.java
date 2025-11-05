package com.etuniesp.fabrica_software.professor;

import com.etuniesp.fabrica_software.professor.dto.ProfessorCreateDTO;
import com.etuniesp.fabrica_software.professor.dto.ProfessorResponseDTO;
import com.etuniesp.fabrica_software.professor.dto.ProfessorUpdateDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/professores")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService service;

    @PostMapping
    public ProfessorResponseDTO criar(@RequestBody @Valid ProfessorCreateDTO dto) {
        return service.criar(dto);
    }

    @PutMapping("/{id}")
    public ProfessorResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid ProfessorUpdateDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }

    @GetMapping("/{id}")
    public ProfessorResponseDTO buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping
    public Page<ProfessorResponseDTO> listar(@RequestParam(required = false) String q, Pageable pageable) {
        return service.listar(q, pageable);
    }
}


