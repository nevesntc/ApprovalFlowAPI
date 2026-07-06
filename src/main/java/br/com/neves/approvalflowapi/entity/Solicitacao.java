package br.com.neves.approvalflowapi.entity;

import br.com.neves.approvalflowapi.entity.StatusSolicitacao;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitacoes")
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeSolicitante;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSolicitacao status;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    protected Solicitacao() {
        // Construtor exigido pelo JPA
    }

    public Solicitacao(String nomeSolicitante, String descricao, BigDecimal valor) { /* Construtor para criar uma nova solicitação */
        if (nomeSolicitante == null || nomeSolicitante.isBlank()) {
            throw new IllegalArgumentException("Nome do solicitante é obrigatório.");
        }

        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descrição é obrigatória.");
        }

        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero.");
        }

        this.nomeSolicitante = nomeSolicitante; // Inicializa o nome do solicitante
        this.descricao = descricao;
        this.valor = valor;
        this.status = StatusSolicitacao.PENDENTE;
        this.dataCriacao = LocalDateTime.now();
    }

    public void aprovar() { // Método para aprovar a solicitação
        if (this.status != StatusSolicitacao.PENDENTE) {
            throw new IllegalStateException("Somente solicitações pendentes podem ser aprovadas.");
        }

        this.status = StatusSolicitacao.APROVADO;
    }

    public void reprovar() { // Método para reprovar a solicitação
        if (this.status != StatusSolicitacao.PENDENTE) {
            throw new IllegalStateException("Somente solicitações pendentes podem ser reprovadas.");
        }

        this.status = StatusSolicitacao.REJEITADO;
    }
    public void atualizar(String descricao, BigDecimal valor) {
        if (this.status != StatusSolicitacao.PENDENTE) {
            throw new IllegalStateException("Somente solicitações pendentes podem ser atualizadas.");
        }

        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descrição é obrigatória.");
        }

        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero.");
        }

        this.descricao = descricao;
        this.valor = valor;
    }

    public Long getId() { // Método para obter o ID da solicitação
        return id;
    }

    public String getNomeSolicitante() {
        return nomeSolicitante;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public StatusSolicitacao getStatus() {
        return status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
}