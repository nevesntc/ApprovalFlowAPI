package br.com.neves.approvalflowapi.exception;

import java.time.LocalDateTime;

public record RespostaErro(
        LocalDateTime dataHora,
        int status,
        String erro,
        String mensagem,
        String caminho
) {
}
