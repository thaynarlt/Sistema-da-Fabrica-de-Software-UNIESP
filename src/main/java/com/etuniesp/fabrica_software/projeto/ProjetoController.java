package com.etuniesp.fabrica_software.projeto;

import com.etuniesp.fabrica_software.projeto.dto.ProjetoCreateDTO;
import com.etuniesp.fabrica_software.projeto.dto.ProjetoResponseDTO;
import com.etuniesp.fabrica_software.projeto.dto.ProjetoUpdateDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projetos")
@RequiredArgsConstructor
public class ProjetoController {

    private final ProjetoService service;

    @PostMapping
    public ProjetoResponseDTO criar(@RequestBody @Valid ProjetoCreateDTO dto) {
        return service.criar(dto);
    }

    @PutMapping("/{id}")
    public ProjetoResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid ProjetoUpdateDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }

    @GetMapping("/{id}")
    public ProjetoResponseDTO buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping
    public Page<ProjetoResponseDTO> listar(@RequestParam(required = false) String q, Pageable pageable) {
        return service.listar(q, pageable);
    }
}


