package br.com.neves.approvalflowapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.neves.approvalflowapi.entity.Solicitacao;
import br.com.neves.approvalflowapi.entity.StatusSolicitacao;

import java.util.List;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {

    List<Solicitacao> findByStatusOrderByDataCriacaoAsc(StatusSolicitacao status);
}
