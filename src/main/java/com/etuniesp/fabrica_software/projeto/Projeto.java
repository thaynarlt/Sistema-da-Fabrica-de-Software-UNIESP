package com.etuniesp.fabrica_software.projeto;

import com.etuniesp.fabrica_software.aluno.Aluno;
// import com.etuniesp.fabrica_software.professor.Professor; // não utilizado para a entrega
import com.etuniesp.fabrica_software.projeto.enums.StatusProjeto;
import com.etuniesp.fabrica_software.projeto.enums.TipoProjeto;
import com.etuniesp.fabrica_software.stack.StackTecnologia;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tb_softfact_projeto", uniqueConstraints = {
        @UniqueConstraint(name = "uk_projeto_titulo_semestre", columnNames = {"titulo", "semestre"})
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "text")
    private String descricao;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String semestre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private TipoProjeto tipo = TipoProjeto.AUTORAL;

    @Column(length = 200)
    private String empresaParceira;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private StatusProjeto status = StatusProjeto.PLANEJADO;

    private LocalDate dataInicio;

    private LocalDate dataFim;

    /*
     * RELACIONAMENTO COM PROFESSOR COMENTADO PARA A ENTREGA
     * 
     * Motivo: O professor solicitou que a entrega do CRUD de Projeto contenha
     * apenas os relacionamentos com Aluno e Stack. O relacionamento com Professor
     * foi mantido comentado na entidade, mas removido dos DTOs e do Service.
     * 
     * A coluna professor_id permanece no banco de dados (migration V3), mas não
     * será utilizada nesta entrega. Pode ser reativado futuramente se necessário.
     */
    // @ManyToOne
    // @JoinColumn(name = "professor_id")
    // private Professor professor;

    @ManyToMany
    @JoinTable(name = "tb_softfact_projeto_aluno",
            joinColumns = @JoinColumn(name = "projeto_id"),
            inverseJoinColumns = @JoinColumn(name = "aluno_id"))
    @Builder.Default
    private Set<Aluno> alunos = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "tb_softfact_projeto_stack",
            joinColumns = @JoinColumn(name = "projeto_id"),
            inverseJoinColumns = @JoinColumn(name = "stack_id"))
    @Builder.Default
    private Set<StackTecnologia> stacksDesejadas = new HashSet<>();
}


