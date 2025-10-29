package com.etuniesp.fabrica_software.aluno;

import com.etuniesp.fabrica_software.aluno.dto.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlunoService {
    AlunoResponseDTO criar(AlunoCreateDTO dto);

    AlunoResponseDTO atualizar(Long id, AlunoUpdateDTO dto);

    void excluir(Long id);

    AlunoResponseDTO buscarPorId(Long id);

    Page<AlunoResponseDTO> listar(String termo, Pageable pageable);
}
