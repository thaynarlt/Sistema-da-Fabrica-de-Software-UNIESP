package com.etuniesp.fabrica_software.aluno.dto;

import com.etuniesp.fabrica_software.aluno.enums.Curso;
import com.etuniesp.fabrica_software.aluno.enums.Periodo;
import com.etuniesp.fabrica_software.stack.dto.StackResumo;

import java.util.Set;

public record AlunoResponseDTO(
        Long id,
        String nome,
        String email,
        String telefone,
        Curso curso,
        String matricula,
        Periodo periodo,
        Set<StackResumo> stacks) {
}
