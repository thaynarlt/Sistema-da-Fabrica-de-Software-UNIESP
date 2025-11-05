package com.etuniesp.fabrica_software.professor;

import com.etuniesp.fabrica_software.professor.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfessorService {
    ProfessorResponseDTO criar(ProfessorCreateDTO dto);

    ProfessorResponseDTO atualizar(Long id, ProfessorUpdateDTO dto);

    void excluir(Long id);

    ProfessorResponseDTO buscarPorId(Long id);

    Page<ProfessorResponseDTO> listar(String termo, Pageable pageable);
}


