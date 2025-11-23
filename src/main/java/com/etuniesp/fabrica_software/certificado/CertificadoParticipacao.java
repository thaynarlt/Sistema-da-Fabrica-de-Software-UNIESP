package com.etuniesp.fabrica_software.certificado;

import com.etuniesp.fabrica_software.alocacao.Alocacao;
import com.etuniesp.fabrica_software.aluno.Aluno;
import com.etuniesp.fabrica_software.projeto.Projeto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "tb_softfact_certificado_part", uniqueConstraints = {
        @UniqueConstraint(name = "uk_cert_part_codigo", columnNames = "codigo_verificacao")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificadoParticipacao {

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

    @ManyToOne(optional = false)
    @JoinColumn(name = "alocacao_id", nullable = false)
    @NotNull
    private Alocacao alocacao;

    @NotBlank
    @Column(name = "codigo_verificacao", nullable = false, length = 80)
    private String codigoVerificacao;

    @NotNull
    @Column(nullable = false)
    @Builder.Default
    private Integer versao = 1; 

    @NotNull
    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true; 

    @Column(name = "pdf_url", length = 300)
    private String pdfUrl; 
}
