package br.com.neves.approvalflowapi.dto;

import br.com.neves.approvalflowapi.entity.StatusSolicitacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SolicitacaoResponseDTO(
        Long id,
        String nomeSolicitante,
        String descricao,
        BigDecimal valor,
        StatusSolicitacao status,
        LocalDateTime dataCriacao
) {
}
