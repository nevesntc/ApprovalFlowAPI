package br.com.neves.approvalflowapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CriarSolicitacaoRequestDTO( /* DTO para criar uma nova solicitação */

        @NotBlank(message = "Nome do solicitante é obrigatório.")
        String nomeSolicitante,

        @NotBlank(message = "Descrição é obrigatória.")
        String descricao,

        @NotNull(message = "Valor é obrigatório.")
        @Positive(message = "Valor deve ser maior que zero.")
        BigDecimal valor
) {
}