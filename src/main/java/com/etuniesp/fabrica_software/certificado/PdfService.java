package com.etuniesp.fabrica_software.certificado;

import com.etuniesp.fabrica_software.certificado.dto.CertificadoParticipacaoResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PdfService {

    private final TemplateEngine templateEngine;

    @Value("${app.certificados.diretorio:./certificados}")
    private String diretorioCertificados;

    @Value("${app.certificados.url-base:http://localhost:8080/api/certificados-participacao}")
    private String urlBase;

    /**
     * Gera PDF do certificado e retorna a URL
     */
    public String gerarPdf(CertificadoParticipacaoResponseDTO certificado) {
        try {
            log.info("Iniciando geração de PDF para certificado ID: {}", certificado.id());

            // 1. Processar template Thymeleaf
            Context context = new Context();
            context.setVariable("alunoNome", certificado.alunoNome() != null ? certificado.alunoNome() : "");
            context.setVariable("projetoTitulo",
                    certificado.projetoTitulo() != null ? certificado.projetoTitulo() : "");
            context.setVariable("semestre", certificado.semestre() != null ? certificado.semestre() : "");
            context.setVariable("papel", certificado.papel() != null ? certificado.papel() : "");
            context.setVariable("cargaHoraria", certificado.cargaHoraria() != null ? certificado.cargaHoraria() : 0);
            context.setVariable("codigoVerificacao",
                    certificado.codigoVerificacao() != null ? certificado.codigoVerificacao() : "");
            context.setVariable("urlVerificacao",
                    urlBase + "/verificar/"
                            + (certificado.codigoVerificacao() != null ? certificado.codigoVerificacao() : ""));

            log.info("Processando template Thymeleaf...");
            String html = templateEngine.process("certificado/certificado-participacao", context);
            log.info("Template processado com sucesso. Tamanho HTML: {} caracteres", html.length());

            // 2. Converter HTML para PDF
            log.info("Convertendo HTML para PDF...");
            byte[] pdfBytes = htmlParaPdf(html);
            log.info("PDF convertido com sucesso. Tamanho: {} bytes", pdfBytes.length);

            // 3. Salvar arquivo
            String nomeArquivo = "certificado-" + certificado.id() + "-" + UUID.randomUUID() + ".pdf";
            Path diretorio = Paths.get(diretorioCertificados);
            log.info("Diretório de certificados: {}", diretorio.toAbsolutePath());

            if (!Files.exists(diretorio)) {
                log.info("Criando diretório: {}", diretorio);
                Files.createDirectories(diretorio);
            }

            Path arquivo = diretorio.resolve(nomeArquivo);
            log.info("Salvando arquivo PDF: {}", arquivo.toAbsolutePath());
            Files.write(arquivo, pdfBytes);

            log.info("PDF gerado com sucesso: {}", arquivo.toAbsolutePath());

            // 4. Retornar URL relativa (será usado para construir URL completa)
            return nomeArquivo;

        } catch (Exception e) {
            log.error("Erro ao gerar PDF do certificado", e);
            throw new RuntimeException("Erro ao gerar PDF do certificado: " + e.getMessage(), e);
        }
    }

    /**
     * Converte HTML em PDF usando Flying Saucer + OpenPDF
     */
    private byte[] htmlParaPdf(String html) throws Exception {
        try {
            log.info("Iniciando conversão HTML para PDF...");
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            log.info("Layout do PDF processado");

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                renderer.createPDF(outputStream);
                byte[] pdfBytes = outputStream.toByteArray();
                log.info("PDF criado com sucesso. Tamanho: {} bytes", pdfBytes.length);
                return pdfBytes;
            }
        } catch (Exception e) {
            log.error("Erro ao converter HTML para PDF. HTML (primeiros 500 chars): {}",
                    html.length() > 500 ? html.substring(0, 500) : html, e);
            throw e;
        }
    }

    /**
     * Busca o arquivo PDF pelo nome
     */
    public Path buscarArquivoPdf(String nomeArquivo) {
        Path diretorio = Paths.get(diretorioCertificados);
        return diretorio.resolve(nomeArquivo);
    }
}
