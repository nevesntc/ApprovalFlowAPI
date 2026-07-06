package br.com.neves.approvalflowapi.mensageria;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SolicitacaoAprovadaEvento(
        Long idSolicitacao,
        String nomeSolicitante,
        BigDecimal valor,
        LocalDateTime dataEvento
) {
}