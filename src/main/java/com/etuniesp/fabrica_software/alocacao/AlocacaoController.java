package com.etuniesp.fabrica_software.alocacao;

import com.etuniesp.fabrica_software.alocacao.dto.AlocacaoCreateDTO;
import com.etuniesp.fabrica_software.alocacao.dto.AlocacaoResponseDTO;
import com.etuniesp.fabrica_software.alocacao.dto.AlocacaoUpdateDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alocacoes")
@RequiredArgsConstructor
public class AlocacaoController {

    private final AlocacaoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlocacaoResponseDTO criar(@RequestBody @Valid AlocacaoCreateDTO dto) {
        return service.criar(dto);
    }

    @PutMapping("/{id}")
    public AlocacaoResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid AlocacaoUpdateDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }

    @GetMapping("/{id}")
    public AlocacaoResponseDTO buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping
    public Page<AlocacaoResponseDTO> listar(@RequestParam(required = false) String q, Pageable pageable) {
        return service.listar(q, pageable);
    }

    @GetMapping("/aluno/{alunoId}")
    public List<AlocacaoResponseDTO> listarPorAluno(@PathVariable Long alunoId) {
        return service.listarPorAluno(alunoId);
    }

    @GetMapping("/projeto/{projetoId}")
    public List<AlocacaoResponseDTO> listarPorProjeto(@PathVariable Long projetoId) {
        return service.listarPorProjeto(projetoId);
    }

    @PutMapping("/{id}/encerrar")
    public AlocacaoResponseDTO encerrar(
            @PathVariable Long id,
            @RequestBody Map<String, Object> dados) {
        Integer cargaHorariaRealizada = dados.get("cargaHorariaRealizada") != null ?
                Integer.valueOf(dados.get("cargaHorariaRealizada").toString()) : null;
        String observacoes = dados.get("observacoes") != null ?
                dados.get("observacoes").toString() : null;
        return service.encerrar(id, cargaHorariaRealizada, observacoes);
    }
}

