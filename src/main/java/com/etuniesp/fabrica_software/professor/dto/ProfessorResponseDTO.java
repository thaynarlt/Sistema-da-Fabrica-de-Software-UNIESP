package com.etuniesp.fabrica_software.professor.dto;

import com.etuniesp.fabrica_software.stack.dto.StackResumo;

import java.util.Set;

public record ProfessorResponseDTO(
        Long id,
        String nome,
        String email,
        String telefone,
        String area,
        Set<StackResumo> stacks
) {}


