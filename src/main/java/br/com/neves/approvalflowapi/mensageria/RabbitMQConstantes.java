package br.com.neves.approvalflowapi.mensageria;

public final class RabbitMQConstantes {

    private RabbitMQConstantes() {
    }

    public static final String EXCHANGE_SOLICITACOES = "solicitacoes.exchange";

    public static final String FILA_SOLICITACAO_APROVADA = "solicitacao.aprovada.queue";
    public static final String FILA_SOLICITACAO_REPROVADA = "solicitacao.reprovada.queue";

    public static final String ROTA_SOLICITACAO_APROVADA = "solicitacao.aprovada";
    public static final String ROTA_SOLICITACAO_REPROVADA = "solicitacao.reprovada";
}