package br.com.neves.approvalflowapi.services;

import br.com.neves.approvalflowapi.mensageria.SolicitacaoEventoPublisher;
import br.com.neves.approvalflowapi.dto.AtualizarSolicitacaoRequestDTO;
import br.com.neves.approvalflowapi.dto.CriarSolicitacaoRequestDTO;
import br.com.neves.approvalflowapi.dto.SolicitacaoResponseDTO;
import br.com.neves.approvalflowapi.entity.Solicitacao;
import br.com.neves.approvalflowapi.entity.StatusSolicitacao;
import br.com.neves.approvalflowapi.exception.RecursoNaoEncontradoException;
import br.com.neves.approvalflowapi.repository.SolicitacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final SolicitacaoEventoPublisher solicitacaoEventoPublisher;

    public SolicitacaoService(
            SolicitacaoRepository solicitacaoRepository,
            SolicitacaoEventoPublisher solicitacaoEventoPublisher
    ) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.solicitacaoEventoPublisher = solicitacaoEventoPublisher;
    }

    public SolicitacaoResponseDTO criar(CriarSolicitacaoRequestDTO dto) {
        Solicitacao solicitacao = new Solicitacao(
                dto.nomeSolicitante(),
                dto.descricao(),
                dto.valor()
        );

        Solicitacao solicitacaoSalva = solicitacaoRepository.save(solicitacao);

        return mapearParaResponse(solicitacaoSalva);
    }
    public SolicitacaoResponseDTO atualizar(Long id, AtualizarSolicitacaoRequestDTO dto) {
        Solicitacao solicitacao = buscarSolicitacaoPorId(id);

        solicitacao.atualizar(
                dto.descricao(),
                dto.valor()
        );

        Solicitacao solicitacaoAtualizada = solicitacaoRepository.save(solicitacao);

        return mapearParaResponse(solicitacaoAtualizada);
    }

    public List<SolicitacaoResponseDTO> listar(StatusSolicitacao status) {
        List<Solicitacao> solicitacoes;

        if (status == null) {
            solicitacoes = solicitacaoRepository.findAll();
        } else {
            solicitacoes = solicitacaoRepository.findByStatusOrderByDataCriacaoAsc(status);
        }

        return solicitacoes.stream()
                .map(this::mapearParaResponse)
                .toList();
    }

    public SolicitacaoResponseDTO buscarPorId(Long id) {
        Solicitacao solicitacao = buscarSolicitacaoPorId(id);

        return mapearParaResponse(solicitacao);
    }

    public void aprovar(Long id) {
        Solicitacao solicitacao = buscarSolicitacaoPorId(id);

        solicitacao.aprovar();

        Solicitacao solicitacaoSalva = solicitacaoRepository.save(solicitacao);

        solicitacaoEventoPublisher.publicarSolicitacaoAprovada(solicitacaoSalva);
    }

    public void reprovar(Long id) {
        Solicitacao solicitacao = buscarSolicitacaoPorId(id);

        solicitacao.reprovar();

        Solicitacao solicitacaoSalva = solicitacaoRepository.save(solicitacao);

        solicitacaoEventoPublisher.publicarSolicitacaoReprovada(solicitacaoSalva);
    }

    private Solicitacao buscarSolicitacaoPorId(Long id) {
        return solicitacaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação não encontrada."));
    }

    private SolicitacaoResponseDTO mapearParaResponse(Solicitacao solicitacao) {
        return new SolicitacaoResponseDTO(
                solicitacao.getId(),
                solicitacao.getNomeSolicitante(),
                solicitacao.getDescricao(),
                solicitacao.getValor(),
                solicitacao.getStatus(),
                solicitacao.getDataCriacao()
        );
    }
}