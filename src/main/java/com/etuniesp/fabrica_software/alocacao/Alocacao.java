package com.etuniesp.fabrica_software.alocacao;

import com.etuniesp.fabrica_software.alocacao.enums.PapelAlocacao;
import com.etuniesp.fabrica_software.alocacao.enums.StatusAlocacao;
import com.etuniesp.fabrica_software.aluno.Aluno;
import com.etuniesp.fabrica_software.projeto.Projeto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "tb_softfact_alocacao")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alocacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "aluno_id", nullable = false)
    @NotNull
    private Aluno aluno;

    @ManyToOne(optional = false)
    @JoinColumn(name = "projeto_id", nullable = false)
    @NotNull
    private Projeto projeto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @NotNull
    private PapelAlocacao papel;

    @Column(nullable = false)
    @NotNull
    private LocalDate dataInicio;

    private LocalDate dataFim;

    @Column(nullable = false)
    @NotNull
    private Integer cargaHorariaPrevista;

    private Integer cargaHorariaRealizada;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private StatusAlocacao status = StatusAlocacao.ATIVA;

    @Column(columnDefinition = "text")
    private String observacoes;
}

