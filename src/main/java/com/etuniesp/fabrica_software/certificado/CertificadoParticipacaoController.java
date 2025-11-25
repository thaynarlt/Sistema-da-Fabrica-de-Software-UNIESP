package com.etuniesp.fabrica_software.certificado;

import com.etuniesp.fabrica_software.certificado.dto.CertificadoParticipacaoCreateDTO;
import com.etuniesp.fabrica_software.certificado.dto.CertificadoParticipacaoResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/certificados-participacao")
@RequiredArgsConstructor
public class CertificadoParticipacaoController {

    private final CertificadoParticipacaoService service;
    private final PdfService pdfService;
    private final CertificadoParticipacaoRepository certificadoRepo;

    /**
     * Emite um certificado de participação para uma alocação.
     * POST /api/certificados-participacao
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CertificadoParticipacaoResponseDTO emitir(@RequestBody @Valid CertificadoParticipacaoCreateDTO dto) {
        return service.emitir(dto);
    }

    /**
     * Download do PDF do certificado.
     * GET /api/certificados-participacao/{id}/pdf
     * Deve vir antes de /{id} para evitar conflito de mapeamento
     */
    @GetMapping("/{id}/pdf")
    public ResponseEntity<Resource> downloadPdf(@PathVariable Long id) {
        CertificadoParticipacao certificado = certificadoRepo.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Certificado não encontrado: " + id));

        if (certificado.getPdfUrl() == null || certificado.getPdfUrl().isBlank()) {
            throw new IllegalStateException("PDF ainda não foi gerado para este certificado");
        }

        Path arquivo = pdfService.buscarArquivoPdf(certificado.getPdfUrl());

        if (!java.nio.file.Files.exists(arquivo)) {
            throw new IllegalStateException("Arquivo PDF não encontrado no servidor");
        }

        Resource resource = new FileSystemResource(arquivo.toFile());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + certificado.getPdfUrl() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    /**
     * Busca certificado por código de verificação (para verificação pública).
     * GET /api/certificados-participacao/verificar/{codigoVerificacao}
     */
    @GetMapping("/verificar/{codigoVerificacao}")
    public CertificadoParticipacaoResponseDTO buscarPorCodigoVerificacao(@PathVariable String codigoVerificacao) {
        return service.buscarPorCodigoVerificacao(codigoVerificacao);
    }

    /**
     * Busca certificado por ID.
     * GET /api/certificados-participacao/{id}
     */
    @GetMapping("/{id}")
    public CertificadoParticipacaoResponseDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    /**
     * Lista todos os certificados de um aluno.
     * GET /api/certificados-participacao/aluno/{alunoId}
     */
    @GetMapping("/aluno/{alunoId}")
    public List<CertificadoParticipacaoResponseDTO> listarPorAluno(@PathVariable Long alunoId) {
        return service.listarPorAluno(alunoId);
    }

    /**
     * Lista apenas certificados ativos de um aluno.
     * GET /api/certificados-participacao/aluno/{alunoId}/ativos
     */
    @GetMapping("/aluno/{alunoId}/ativos")
    public List<CertificadoParticipacaoResponseDTO> listarAtivosPorAluno(@PathVariable Long alunoId) {
        return service.listarAtivosPorAluno(alunoId);
    }

    /**
     * Lista todos os certificados de um projeto.
     * GET /api/certificados-participacao/projeto/{projetoId}
     */
    @GetMapping("/projeto/{projetoId}")
    public List<CertificadoParticipacaoResponseDTO> listarPorProjeto(@PathVariable Long projetoId) {
        return service.listarPorProjeto(projetoId);
    }

    /**
     * Republica um certificado (cria nova versão e desativa a anterior).
     * POST /api/certificados-participacao/{id}/republicar
     */
    @PostMapping("/{id}/republicar")
    public CertificadoParticipacaoResponseDTO republicar(@PathVariable Long id) {
        return service.republicar(id);
    }

    /**
     * Desativa um certificado (marca como inativo).
     * PATCH /api/certificados-participacao/{id}/desativar
     */
    @PatchMapping("/{id}/desativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable Long id) {
        service.desativar(id);
    }

    /**
     * Gera ou regenera o PDF de um certificado existente.
     * POST /api/certificados-participacao/{id}/gerar-pdf
     */
    @PostMapping("/{id}/gerar-pdf")
    public CertificadoParticipacaoResponseDTO gerarPdf(@PathVariable Long id) {
        return service.gerarPdf(id);
    }
}

