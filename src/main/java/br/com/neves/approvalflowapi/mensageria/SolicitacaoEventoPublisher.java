package br.com.neves.approvalflowapi.mensageria;

import br.com.neves.approvalflowapi.entity.Solicitacao;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static br.com.neves.approvalflowapi.mensageria.RabbitMQConstantes.*;

@Component
public class SolicitacaoEventoPublisher {

    private final RabbitTemplate rabbitTemplate;

    public SolicitacaoEventoPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicarSolicitacaoAprovada(Solicitacao solicitacao) {
        SolicitacaoAprovadaEvento evento = new SolicitacaoAprovadaEvento(
                solicitacao.getId(),
                solicitacao.getNomeSolicitante(),
                solicitacao.getValor(),
                LocalDateTime.now()
        );

        rabbitTemplate.convertAndSend(
                EXCHANGE_SOLICITACOES,
                ROTA_SOLICITACAO_APROVADA,
                evento
        );
    }

    public void publicarSolicitacaoReprovada(Solicitacao solicitacao) {
        SolicitacaoReprovadaEvento evento = new SolicitacaoReprovadaEvento(
                solicitacao.getId(),
                solicitacao.getNomeSolicitante(),
                solicitacao.getValor(),
                LocalDateTime.now()
        );

        rabbitTemplate.convertAndSend(
                EXCHANGE_SOLICITACOES,
                ROTA_SOLICITACAO_REPROVADA,
                evento
        );
    }
}