package br.com.neves.approvalflowapi.controller;

import br.com.neves.approvalflowapi.dto.AtualizarSolicitacaoRequestDTO;
import br.com.neves.approvalflowapi.dto.CriarSolicitacaoRequestDTO;
import br.com.neves.approvalflowapi.dto.SolicitacaoResponseDTO;
import br.com.neves.approvalflowapi.entity.StatusSolicitacao;
import br.com.neves.approvalflowapi.services.SolicitacaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/solicitacoes")
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;

    public SolicitacaoController(SolicitacaoService solicitacaoService) {
        this.solicitacaoService = solicitacaoService;
    }

    @PostMapping
    public ResponseEntity<SolicitacaoResponseDTO> criar(
            @RequestBody @Valid CriarSolicitacaoRequestDTO dto
    ) {
        SolicitacaoResponseDTO solicitacaoCriada = solicitacaoService.criar(dto);

        URI location = URI.create("/api/solicitacoes/" + solicitacaoCriada.id());

        return ResponseEntity.created(location).body(solicitacaoCriada);
    }
    @PutMapping("/{id}")
    public ResponseEntity<SolicitacaoResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AtualizarSolicitacaoRequestDTO dto
    ) {
        SolicitacaoResponseDTO solicitacaoAtualizada = solicitacaoService.atualizar(id, dto);

        return ResponseEntity.ok(solicitacaoAtualizada);
    }

    @GetMapping
    public ResponseEntity<List<SolicitacaoResponseDTO>> listar(
            @RequestParam(required = false) StatusSolicitacao status
    ) {
        List<SolicitacaoResponseDTO> solicitacoes = solicitacaoService.listar(status);

        return ResponseEntity.ok(solicitacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoResponseDTO> buscarPorId(@PathVariable Long id) {
        SolicitacaoResponseDTO solicitacao = solicitacaoService.buscarPorId(id);

        return ResponseEntity.ok(solicitacao);
    }

    @PatchMapping("/{id}/aprovar")
    public ResponseEntity<Void> aprovar(@PathVariable Long id) {
        solicitacaoService.aprovar(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reprovar")
    public ResponseEntity<Void> reprovar(@PathVariable Long id) {
        solicitacaoService.reprovar(id);

        return ResponseEntity.noContent().build();
    }
}