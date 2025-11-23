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
}
