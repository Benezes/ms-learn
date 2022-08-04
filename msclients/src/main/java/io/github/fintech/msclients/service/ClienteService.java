package io.github.fintech.msclients.service;

import io.github.fintech.msclients.domain.Cliente;
import io.github.fintech.msclients.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public Cliente save(final Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente getByCpf(final String cpf) {
        return clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("CPF: " + cpf + ", não encontrado"));
    }
}