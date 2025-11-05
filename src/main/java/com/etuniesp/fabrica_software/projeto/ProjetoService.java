package com.etuniesp.fabrica_software.projeto;

import com.etuniesp.fabrica_software.projeto.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjetoService {
    ProjetoResponseDTO criar(ProjetoCreateDTO dto);

    ProjetoResponseDTO atualizar(Long id, ProjetoUpdateDTO dto);

    void excluir(Long id);

    ProjetoResponseDTO buscarPorId(Long id);

    Page<ProjetoResponseDTO> listar(String termo, Pageable pageable);
}


