package io.github.fintech.mscartoes.service;

import io.github.fintech.mscartoes.domain.ClienteCartao;
import io.github.fintech.mscartoes.repository.ClienteCartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteCartaoService {

    private final ClienteCartaoRepository cartaoRepository;

    public List<ClienteCartao> listCartoesByCpf(final String cpf) {
        return cartaoRepository.findByCpf(cpf);
    }
}
