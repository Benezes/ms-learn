package io.github.fintech.mscartoes.controller;

import io.github.fintech.mscartoes.domain.Cartao;
import io.github.fintech.mscartoes.dto.CartaoSaveRequest;
import io.github.fintech.mscartoes.dto.CartoesPorClienteResponse;
import io.github.fintech.mscartoes.service.CartaoService;
import io.github.fintech.mscartoes.service.ClienteCartaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cartoes")
@RequiredArgsConstructor
public class CartoesController {

    private final CartaoService cartaoService;
    private final ClienteCartaoService cartaoCartaoService;

    @GetMapping
    public String status() {
        return "OK";
    }

    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody CartaoSaveRequest cartaoRequest) {
        cartaoService.save(cartaoRequest.toModel());
        return ResponseEntity.status(HttpStatus.CREATED).body("CARTÃO CRIADO!");
    }

    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCartoesRendaAte(@RequestParam("renda") Long renda) {
        return ResponseEntity.ok(cartaoService.getCartoesRendaMenorIgual(renda));
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<List<CartoesPorClienteResponse>> getCartoesByCliente(@RequestParam("cpf") String cpf) {
        List<CartoesPorClienteResponse> list =
                cartaoCartaoService.listCartoesByCpf(cpf)
                        .stream().map(CartoesPorClienteResponse::fromModel).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }
}
