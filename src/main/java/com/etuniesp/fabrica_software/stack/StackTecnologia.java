package com.etuniesp.fabrica_software.stack;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_softfact_stack", uniqueConstraints = {
        @UniqueConstraint(name = "uk_stack_nome", columnNames = "nome")
})
public class StackTecnologia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 80)
    private String nome;

    @Column(length = 40)
    private String categoria;
}
