package io.github.fintech.mscartoes.service;

import io.github.fintech.mscartoes.domain.Cartao;
import io.github.fintech.mscartoes.repository.CartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartaoService {

    private final CartaoRepository cartaoRepository;

    @Transactional
    public Cartao save(final Cartao cartao) {
        return cartaoRepository.save(cartao);
    }

    public List<Cartao> getCartoesRendaMenorIgual(final Long renda) {
        BigDecimal rendaBigdecimal = BigDecimal.valueOf(renda);
        List<Cartao> cartoes = cartaoRepository.findByRendaLessThanEqual(rendaBigdecimal);
        return cartoes;
    }
}
