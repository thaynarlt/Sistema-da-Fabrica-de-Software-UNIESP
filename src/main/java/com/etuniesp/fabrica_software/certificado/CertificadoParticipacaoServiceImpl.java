package com.etuniesp.fabrica_software.certificado;

import com.etuniesp.fabrica_software.alocacao.Alocacao;
import com.etuniesp.fabrica_software.alocacao.AlocacaoRepository;
import com.etuniesp.fabrica_software.alocacao.enums.StatusAlocacao;
import com.etuniesp.fabrica_software.certificado.dto.CertificadoParticipacaoCreateDTO;
import com.etuniesp.fabrica_software.certificado.dto.CertificadoParticipacaoResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CertificadoParticipacaoServiceImpl implements CertificadoParticipacaoService {

    private final CertificadoParticipacaoRepository repo;
    private final AlocacaoRepository alocacaoRepo;
    private final PdfService pdfService;

    @Override
    public CertificadoParticipacaoResponseDTO emitir(CertificadoParticipacaoCreateDTO dto) {
        // Buscar alocação
        Alocacao alocacao = alocacaoRepo.findById(dto.alocacaoId())
                .orElseThrow(() -> new EntityNotFoundException("Alocação não encontrada: " + dto.alocacaoId()));

        // Validar se alocação está concluída
        if (alocacao.getStatus() != StatusAlocacao.CONCLUIDA) {
            throw new IllegalArgumentException(
                    "Certificado só pode ser emitido para alocações CONCLUIDAS. Status atual: " + alocacao.getStatus());
        }

        // Validar se tem carga horária registrada
        if (alocacao.getCargaHorariaRealizada() == null || alocacao.getCargaHorariaRealizada() <= 0) {
            throw new IllegalArgumentException(
                    "Certificado só pode ser emitido se houver carga horária realizada registrada");
        }

        // Verificar se já existe certificado ativo para esta alocação
        repo.findAtivoByAlocacaoId(dto.alocacaoId())
                .ifPresent(c -> {
                    throw new DataIntegrityViolationException(
                            "Já existe um certificado ativo para esta alocação. Use republicar para criar nova versão.");
                });

        // Gerar código de verificação único
        String codigoVerificacao = gerarCodigoVerificacao(alocacao);

        // Criar certificado
        CertificadoParticipacao certificado = CertificadoParticipacao.builder()
                .aluno(alocacao.getAluno())
                .projeto(alocacao.getProjeto())
                .alocacao(alocacao)
                .codigoVerificacao(codigoVerificacao)
                .versao(1)
                .ativo(true)
                .pdfUrl(null) // Será preenchido quando o PDF for gerado
                .build();

        CertificadoParticipacao salvo = repo.save(certificado);
        
        // Gerar PDF
        try {
            CertificadoParticipacaoResponseDTO response = toResponse(salvo);
            String pdfUrl = pdfService.gerarPdf(response);
            
            // Atualizar certificado com URL do PDF
            salvo.setPdfUrl(pdfUrl);
            repo.save(salvo);
            
            log.info("PDF gerado com sucesso para certificado ID: {}", salvo.getId());
            return toResponse(salvo);
        } catch (Exception e) {
            // Log do erro detalhado
            log.error("Erro ao gerar PDF para certificado ID: {}. Erro: {}", salvo.getId(), e.getMessage(), e);
            // Não falha a emissão, mas registra o erro
            // O PDF pode ser gerado depois via endpoint específico
            return toResponse(salvo);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CertificadoParticipacaoResponseDTO buscarPorId(Long id) {
        return repo.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Certificado não encontrado: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public CertificadoParticipacaoResponseDTO buscarPorCodigoVerificacao(String codigoVerificacao) {
        return repo.findByCodigoVerificacao(codigoVerificacao)
                .map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Certificado não encontrado com código: " + codigoVerificacao));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificadoParticipacaoResponseDTO> listarPorAluno(Long alunoId) {
        return repo.findByAlunoId(alunoId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificadoParticipacaoResponseDTO> listarPorProjeto(Long projetoId) {
        return repo.findByProjetoId(projetoId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificadoParticipacaoResponseDTO> listarAtivosPorAluno(Long alunoId) {
        return repo.findAtivosByAlunoId(alunoId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CertificadoParticipacaoResponseDTO republicar(Long id) {
        // Buscar certificado existente
        CertificadoParticipacao existente = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Certificado não encontrado: " + id));

        // Desativar certificado anterior
        existente.setAtivo(false);
        repo.save(existente);

        // Buscar alocação
        Alocacao alocacao = existente.getAlocacao();

        // Gerar novo código de verificação
        String novoCodigo = gerarCodigoVerificacao(alocacao);

        // Determinar próxima versão
        Integer proximaVersao = existente.getVersao() + 1;

        // Criar nova versão
        CertificadoParticipacao novaVersao = CertificadoParticipacao.builder()
                .aluno(alocacao.getAluno())
                .projeto(alocacao.getProjeto())
                .alocacao(alocacao)
                .codigoVerificacao(novoCodigo)
                .versao(proximaVersao)
                .ativo(true)
                .pdfUrl(null)
                .build();

        CertificadoParticipacao salvo = repo.save(novaVersao);
        
        // Gerar PDF para nova versão
        try {
            CertificadoParticipacaoResponseDTO response = toResponse(salvo);
            String pdfUrl = pdfService.gerarPdf(response);
            
            // Atualizar certificado com URL do PDF
            salvo.setPdfUrl(pdfUrl);
            repo.save(salvo);
            
            return toResponse(salvo);
        } catch (Exception e) {
            // Log do erro, mas não falha a republicação
            // O PDF pode ser gerado depois via endpoint específico
            return toResponse(salvo);
        }
    }

    @Override
    public void desativar(Long id) {
        CertificadoParticipacao certificado = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Certificado não encontrado: " + id));

        certificado.setAtivo(false);
        repo.save(certificado);
    }

    @Override
    public CertificadoParticipacaoResponseDTO gerarPdf(Long id) {
        CertificadoParticipacao certificado = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Certificado não encontrado: " + id));

        try {
            // Se já existe PDF antigo, deletar o arquivo antes de gerar novo
            if (certificado.getPdfUrl() != null && !certificado.getPdfUrl().isBlank()) {
                try {
                    java.nio.file.Path arquivoAntigo = pdfService.buscarArquivoPdf(certificado.getPdfUrl());
                    if (java.nio.file.Files.exists(arquivoAntigo)) {
                        java.nio.file.Files.delete(arquivoAntigo);
                        log.info("PDF antigo deletado: {}", arquivoAntigo);
                    }
                } catch (Exception e) {
                    log.warn("Não foi possível deletar PDF antigo: {}", e.getMessage());
                    // Continua mesmo se não conseguir deletar
                }
            }
            
            CertificadoParticipacaoResponseDTO response = toResponse(certificado);
            String pdfUrl = pdfService.gerarPdf(response);
            
            // Atualizar certificado com URL do PDF
            certificado.setPdfUrl(pdfUrl);
            repo.save(certificado);
            
            log.info("PDF gerado com sucesso para certificado ID: {}", id);
            return toResponse(certificado);
        } catch (Exception e) {
            log.error("Erro ao gerar PDF para certificado ID: {}. Erro: {}", id, e.getMessage(), e);
            throw new RuntimeException("Erro ao gerar PDF: " + e.getMessage(), e);
        }
    }

    /**
     * Gera um código de verificação único baseado em dados da alocação.
     * Usa SHA-256 para gerar hash único. - código de tamanho fixo de 64 caracteres
     */
    private String gerarCodigoVerificacao(Alocacao alocacao) {
        try {
            // Dados únicos para gerar o hash
            String dados = String.format("%d-%d-%d-%s-%d",
                    alocacao.getId(),
                    alocacao.getAluno().getId(),
                    alocacao.getProjeto().getId(),
                    LocalDateTime.now().toString(),
                    System.nanoTime());

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(dados.getBytes(StandardCharsets.UTF_8));

            // Converter para hexadecimal e pegar primeiros 64 caracteres
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            // Retornar código único (64 caracteres)
            String codigo = hexString.toString();

            // Verificar se já existe (muito improvável, mas por segurança)
            int tentativas = 0;
            while (repo.findByCodigoVerificacao(codigo).isPresent() && tentativas < 10) {
                // Se existir, adicionar timestamp e tentar novamente
                dados = dados + "-" + System.currentTimeMillis();
                hash = digest.digest(dados.getBytes(StandardCharsets.UTF_8));
                hexString = new StringBuilder();
                for (byte b : hash) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) {
                        hexString.append('0');
                    }
                    hexString.append(hex);
                }
                codigo = hexString.toString();
                tentativas++;
            }

            return codigo;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar código de verificação", e);
        }
    }

    /**
     * Converte entidade para DTO de resposta.
     * Não inclui campos de Professor, conforme solicitado.
     */
    private CertificadoParticipacaoResponseDTO toResponse(CertificadoParticipacao c) {
        return new CertificadoParticipacaoResponseDTO(
                c.getId(),
                // Dados do Aluno
                c.getAluno().getId(),
                c.getAluno().getNome(),
                // Dados do Projeto
                c.getProjeto().getId(),
                c.getProjeto().getTitulo(),
                c.getProjeto().getSemestre(),
                // Dados da Alocacao
                c.getAlocacao().getId(),
                c.getAlocacao().getPapel().toString(),
                c.getAlocacao().getCargaHorariaRealizada() != null
                        ? c.getAlocacao().getCargaHorariaRealizada()
                        : c.getAlocacao().getCargaHorariaPrevista(),
                // Dados do Certificado
                c.getCodigoVerificacao(),
                c.getVersao(),
                c.getAtivo(),
                c.getPdfUrl()
        );
    }
}

