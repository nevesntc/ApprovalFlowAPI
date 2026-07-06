package br.com.neves.approvalflowapi.services;

import br.com.neves.approvalflowapi.dto.AtualizarSolicitacaoRequestDTO;
import br.com.neves.approvalflowapi.dto.CriarSolicitacaoRequestDTO;
import br.com.neves.approvalflowapi.dto.SolicitacaoResponseDTO;
import br.com.neves.approvalflowapi.entity.Solicitacao;
import br.com.neves.approvalflowapi.entity.StatusSolicitacao;
import br.com.neves.approvalflowapi.exception.RecursoNaoEncontradoException;
import br.com.neves.approvalflowapi.mensageria.SolicitacaoEventoPublisher;
import br.com.neves.approvalflowapi.repository.SolicitacaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SolicitacaoServiceTest {

    @Mock
    private SolicitacaoRepository solicitacaoRepository;

    @Mock
    private SolicitacaoEventoPublisher solicitacaoEventoPublisher;

    @InjectMocks
    private SolicitacaoService solicitacaoService;

    @Test
    void deveCriarSolicitacaoComStatusPendente() {
        CriarSolicitacaoRequestDTO dto = new CriarSolicitacaoRequestDTO(
                "Bruno Neves",
                "Compra de notebook para desenvolvimento",
                new BigDecimal("4500.00")
        );

        Solicitacao solicitacaoSalva = new Solicitacao(
                dto.nomeSolicitante(),
                dto.descricao(),
                dto.valor()
        );

        given(solicitacaoRepository.save(any(Solicitacao.class)))
                .willReturn(solicitacaoSalva);

        SolicitacaoResponseDTO response = solicitacaoService.criar(dto);

        assertThat(response.nomeSolicitante()).isEqualTo("Bruno Neves");
        assertThat(response.descricao()).isEqualTo("Compra de notebook para desenvolvimento");
        assertThat(response.valor()).isEqualByComparingTo("4500.00");
        assertThat(response.status()).isEqualTo(StatusSolicitacao.PENDENTE);
        assertThat(response.dataCriacao()).isNotNull();

        verify(solicitacaoRepository).save(any(Solicitacao.class));
    }

    @Test
    void deveListarTodasSolicitacoesQuandoStatusNaoForInformado() {
        Solicitacao solicitacao1 = new Solicitacao(
                "Bruno Neves",
                "Compra de notebook",
                new BigDecimal("4500.00")
        );

        Solicitacao solicitacao2 = new Solicitacao(
                "Maria Silva",
                "Reembolso de curso",
                new BigDecimal("800.00")
        );

        given(solicitacaoRepository.findAll())
                .willReturn(List.of(solicitacao1, solicitacao2));

        List<SolicitacaoResponseDTO> response = solicitacaoService.listar(null);

        assertThat(response).hasSize(2);
        assertThat(response.get(0).nomeSolicitante()).isEqualTo("Bruno Neves");
        assertThat(response.get(1).nomeSolicitante()).isEqualTo("Maria Silva");

        verify(solicitacaoRepository).findAll();
    }

    @Test
    void deveListarSolicitacoesFiltrandoPorStatus() {
        Solicitacao solicitacao = new Solicitacao(
                "Bruno Neves",
                "Compra de notebook",
                new BigDecimal("4500.00")
        );

        given(solicitacaoRepository.findByStatusOrderByDataCriacaoAsc(StatusSolicitacao.PENDENTE))
                .willReturn(List.of(solicitacao));

        List<SolicitacaoResponseDTO> response = solicitacaoService.listar(StatusSolicitacao.PENDENTE);

        assertThat(response).hasSize(1);
        assertThat(response.get(0).status()).isEqualTo(StatusSolicitacao.PENDENTE);

        verify(solicitacaoRepository).findByStatusOrderByDataCriacaoAsc(StatusSolicitacao.PENDENTE);
    }

    @Test
    void deveBuscarSolicitacaoPorId() {
        Long id = 1L;

        Solicitacao solicitacao = new Solicitacao(
                "Bruno Neves",
                "Compra de notebook",
                new BigDecimal("4500.00")
        );

        given(solicitacaoRepository.findById(id))
                .willReturn(Optional.of(solicitacao));

        SolicitacaoResponseDTO response = solicitacaoService.buscarPorId(id);

        assertThat(response.nomeSolicitante()).isEqualTo("Bruno Neves");
        assertThat(response.descricao()).isEqualTo("Compra de notebook");
        assertThat(response.valor()).isEqualByComparingTo("4500.00");
        assertThat(response.status()).isEqualTo(StatusSolicitacao.PENDENTE);

        verify(solicitacaoRepository).findById(id);
    }

    @Test
    void deveLancarExcecaoQuandoSolicitacaoNaoForEncontrada() {
        Long id = 99L;

        given(solicitacaoRepository.findById(id))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> solicitacaoService.buscarPorId(id))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Solicitação não encontrada.");

        verify(solicitacaoRepository).findById(id);
    }

    @Test
    void deveAtualizarSolicitacaoPendente() {
        Long id = 1L;

        Solicitacao solicitacao = new Solicitacao(
                "Bruno Neves",
                "Compra de notebook",
                new BigDecimal("4500.00")
        );

        AtualizarSolicitacaoRequestDTO dto = new AtualizarSolicitacaoRequestDTO(
                "Compra de notebook para desenvolvimento Java e Angular",
                new BigDecimal("4800.00")
        );

        given(solicitacaoRepository.findById(id))
                .willReturn(Optional.of(solicitacao));

        given(solicitacaoRepository.save(solicitacao))
                .willReturn(solicitacao);

        SolicitacaoResponseDTO response = solicitacaoService.atualizar(id, dto);

        assertThat(response.descricao()).isEqualTo("Compra de notebook para desenvolvimento Java e Angular");
        assertThat(response.valor()).isEqualByComparingTo("4800.00");
        assertThat(response.status()).isEqualTo(StatusSolicitacao.PENDENTE);

        verify(solicitacaoRepository).findById(id);
        verify(solicitacaoRepository).save(solicitacao);
    }

    @Test
    void naoDeveAtualizarSolicitacaoAprovada() {
        Long id = 1L;

        Solicitacao solicitacao = new Solicitacao(
                "Bruno Neves",
                "Compra de notebook",
                new BigDecimal("4500.00")
        );

        solicitacao.aprovar();

        AtualizarSolicitacaoRequestDTO dto = new AtualizarSolicitacaoRequestDTO(
                "Tentativa de alteração",
                new BigDecimal("3000.00")
        );

        given(solicitacaoRepository.findById(id))
                .willReturn(Optional.of(solicitacao));

        assertThatThrownBy(() -> solicitacaoService.atualizar(id, dto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Somente solicitações pendentes podem ser atualizadas.");

        verify(solicitacaoRepository).findById(id);
    }

    @Test
    void deveAprovarSolicitacaoPendente() {
        Long id = 1L;

        Solicitacao solicitacao = new Solicitacao(
                "Bruno Neves",
                "Compra de notebook",
                new BigDecimal("4500.00")
        );

        given(solicitacaoRepository.findById(id))
                .willReturn(Optional.of(solicitacao));

        given(solicitacaoRepository.save(solicitacao))
                .willReturn(solicitacao);

        solicitacaoService.aprovar(id);

        assertThat(solicitacao.getStatus()).isEqualTo(StatusSolicitacao.APROVADO);

        verify(solicitacaoRepository).findById(id);
        verify(solicitacaoRepository).save(solicitacao);
        verify(solicitacaoEventoPublisher).publicarSolicitacaoAprovada(solicitacao);
    }

    @Test
    void naoDeveAprovarSolicitacaoJaAprovada() {
        Long id = 1L;

        Solicitacao solicitacao = new Solicitacao(
                "Bruno Neves",
                "Compra de notebook",
                new BigDecimal("4500.00")
        );

        solicitacao.aprovar();

        given(solicitacaoRepository.findById(id))
                .willReturn(Optional.of(solicitacao));

        assertThatThrownBy(() -> solicitacaoService.aprovar(id))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Somente solicitações pendentes podem ser aprovadas.");

        verify(solicitacaoRepository).findById(id);
    }

    @Test
    void deveReprovarSolicitacaoPendente() {
        Long id = 1L;

        Solicitacao solicitacao = new Solicitacao(
                "Bruno Neves",
                "Compra de notebook",
                new BigDecimal("4500.00")
        );

        given(solicitacaoRepository.findById(id))
                .willReturn(Optional.of(solicitacao));

        given(solicitacaoRepository.save(solicitacao))
                .willReturn(solicitacao);

        solicitacaoService.reprovar(id);

        assertThat(solicitacao.getStatus()).isEqualTo(StatusSolicitacao.REJEITADO);

        verify(solicitacaoRepository).findById(id);
        verify(solicitacaoRepository).save(solicitacao);
        verify(solicitacaoEventoPublisher).publicarSolicitacaoReprovada(solicitacao);
    }

    @Test
    void naoDeveReprovarSolicitacaoJaReprovada() {
        Long id = 1L;

        Solicitacao solicitacao = new Solicitacao(
                "Bruno Neves",
                "Compra de notebook",
                new BigDecimal("4500.00")
        );

        solicitacao.reprovar();

        given(solicitacaoRepository.findById(id))
                .willReturn(Optional.of(solicitacao));

        assertThatThrownBy(() -> solicitacaoService.reprovar(id))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Somente solicitações pendentes podem ser reprovadas.");

        verify(solicitacaoRepository).findById(id);
    }
}