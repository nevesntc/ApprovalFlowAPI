package br.com.neves.approvalflowapi.mensageria;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static br.com.neves.approvalflowapi.mensageria.RabbitMQConstantes.*;

@Configuration
public class RabbitMQConfig {

    @Bean
    public DirectExchange solicitacoesExchange() {
        return new DirectExchange(EXCHANGE_SOLICITACOES);
    }

    @Bean
    public Queue filaSolicitacaoAprovada() {
        return new Queue(FILA_SOLICITACAO_APROVADA, true);
    }

    @Bean
    public Queue filaSolicitacaoReprovada() {
        return new Queue(FILA_SOLICITACAO_REPROVADA, true);
    }

    @Bean
    public Binding bindingSolicitacaoAprovada() {
        return BindingBuilder
                .bind(filaSolicitacaoAprovada())
                .to(solicitacoesExchange())
                .with(ROTA_SOLICITACAO_APROVADA);
    }

    @Bean
    public Binding bindingSolicitacaoReprovada() {
        return BindingBuilder
                .bind(filaSolicitacaoReprovada())
                .to(solicitacoesExchange())
                .with(ROTA_SOLICITACAO_REPROVADA);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}