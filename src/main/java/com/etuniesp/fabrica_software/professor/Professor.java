package com.etuniesp.fabrica_software.professor;

import com.etuniesp.fabrica_software.stack.StackTecnologia;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tb_softfact_professor", uniqueConstraints = {
        @UniqueConstraint(name = "uk_professor_email", columnNames = "email")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @Email
    @Column(nullable = false)
    private String email;

    private String telefone;

    private String area;

    @ManyToMany
    @JoinTable(name = "tb_softfact_professor_stack",
            joinColumns = @JoinColumn(name = "professor_id"),
            inverseJoinColumns = @JoinColumn(name = "stack_id"))
    @Builder.Default
    private Set<StackTecnologia> stacks = new HashSet<>();
}


