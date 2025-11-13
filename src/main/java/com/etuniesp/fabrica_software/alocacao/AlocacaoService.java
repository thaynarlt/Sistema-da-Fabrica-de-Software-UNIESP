package com.etuniesp.fabrica_software.alocacao;

import com.etuniesp.fabrica_software.alocacao.dto.AlocacaoCreateDTO;
import com.etuniesp.fabrica_software.alocacao.dto.AlocacaoResponseDTO;
import com.etuniesp.fabrica_software.alocacao.dto.AlocacaoUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AlocacaoService {

    AlocacaoResponseDTO criar(AlocacaoCreateDTO dto);

    AlocacaoResponseDTO atualizar(Long id, AlocacaoUpdateDTO dto);

    void excluir(Long id);

    AlocacaoResponseDTO buscarPorId(Long id);

    Page<AlocacaoResponseDTO> listar(String termo, Pageable pageable);

    List<AlocacaoResponseDTO> listarPorAluno(Long alunoId);

    List<AlocacaoResponseDTO> listarPorProjeto(Long projetoId);

    AlocacaoResponseDTO encerrar(Long id, Integer cargaHorariaRealizada, String observacoes);
}

