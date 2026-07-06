package br.com.neves.approvalflowapi.mensageria;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static br.com.neves.approvalflowapi.mensageria.RabbitMQConstantes.*;

@Component
public class SolicitacaoEventoConsumer {

    @RabbitListener(queues = FILA_SOLICITACAO_APROVADA)
    public void consumirSolicitacaoAprovada(SolicitacaoAprovadaEvento evento) {
        System.out.println("Evento recebido: solicitação aprovada");
        System.out.println("ID: " + evento.idSolicitacao());
        System.out.println("Solicitante: " + evento.nomeSolicitante());
        System.out.println("Valor: " + evento.valor());
        System.out.println("Data do evento: " + evento.dataEvento());
    }

    @RabbitListener(queues = FILA_SOLICITACAO_REPROVADA)
    public void consumirSolicitacaoReprovada(SolicitacaoReprovadaEvento evento) {
        System.out.println("Evento recebido: solicitação reprovada");
        System.out.println("ID: " + evento.idSolicitacao());
        System.out.println("Solicitante: " + evento.nomeSolicitante());
        System.out.println("Valor: " + evento.valor());
        System.out.println("Data do evento: " + evento.dataEvento());
    }
}