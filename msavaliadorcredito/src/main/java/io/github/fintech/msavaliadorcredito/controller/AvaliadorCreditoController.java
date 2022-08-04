package io.github.fintech.msavaliadorcredito.controller;

import io.github.fintech.msavaliadorcredito.dto.DadosAvaliacao;
import io.github.fintech.msavaliadorcredito.dto.RetornoAvaliacaoCliente;
import io.github.fintech.msavaliadorcredito.dto.SituacaoCliente;
import io.github.fintech.msavaliadorcredito.service.AvaliadorCreditoService;
import io.github.fintech.msavaliadorcredito.service.exceptions.DadosClienteNotFoundException;
import io.github.fintech.msavaliadorcredito.service.exceptions.ErroComunicacaoMicroservicesException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/avaliacoes-credito")
@RequiredArgsConstructor
public class AvaliadorCreditoController {

    private final AvaliadorCreditoService avaliadorCreditoService;

    @GetMapping
    public String status() {
        return "OK";
    }

    @GetMapping(value = "/situacao-cliente", params = "cpf")
    public ResponseEntity consultaSituacaoCliente(@RequestParam("cpf") String cpf) {
        SituacaoCliente situacaoCliente = null;
        try {
            situacaoCliente = avaliadorCreditoService.obterSituacaoCliente(cpf);
        } catch (DadosClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicroservicesException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
        return ResponseEntity.ok(situacaoCliente);
    }

    @PostMapping
    public ResponseEntity realizarAvaliacao(@RequestBody DadosAvaliacao dados) {
        RetornoAvaliacaoCliente retornoAvaliacaoCliente = null;
        try {
            retornoAvaliacaoCliente = avaliadorCreditoService.realizarAvalicao(dados.getCpf(), dados.getRenda());

        } catch (DadosClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicroservicesException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }

        return ResponseEntity.ok(retornoAvaliacaoCliente);
    }
}
