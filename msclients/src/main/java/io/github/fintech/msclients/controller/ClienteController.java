package io.github.fintech.msclients.controller;

import io.github.fintech.msclients.domain.Cliente;
import io.github.fintech.msclients.dto.ClienteSaveRequeset;
import io.github.fintech.msclients.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public String status() {
        log.info("VERIFICANDO SE TUDO ESTA FUNCIONANDO");
        return "OK";
    }

    @PostMapping
    public ResponseEntity<Cliente> save(@RequestBody final ClienteSaveRequeset cliente) {
        Cliente clienteModel = cliente.toModel();
        clienteService.save(clienteModel);

        URI headerLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .query("cpf={cpf}")
                .buildAndExpand(clienteModel.getCpf())
                .toUri();

        return ResponseEntity.created(headerLocation).build();
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<Cliente> dadosCliente(@RequestParam("cpf") final String cpf) {
        return ResponseEntity.ok(clienteService.getByCpf(cpf));
    }
}
