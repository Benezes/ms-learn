package io.github.fintech.mscartoes.infra;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.fintech.mscartoes.domain.Cartao;
import io.github.fintech.mscartoes.domain.ClienteCartao;
import io.github.fintech.mscartoes.dto.DadosSolicitacaoEmissaoCartao;
import io.github.fintech.mscartoes.repository.CartaoRepository;
import io.github.fintech.mscartoes.repository.ClienteCartaoRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmissaoCartaoSubscriber {

    private final ObjectMapper mapper;
    private final CartaoRepository cartaoRepository;
    private final ClienteCartaoRepository clienteCartaoRepository;

    @RabbitListener(queues = "${mq.queues.emissao-cartoes}")
    public void receberSolicitacaoEmissao(@Payload String payload) {
        try {
            DadosSolicitacaoEmissaoCartao dados = mapper.readValue(payload, DadosSolicitacaoEmissaoCartao.class);
            Cartao cartao = cartaoRepository.findById(dados.getIdCartao()).orElseThrow();

            ClienteCartao clienteCartao = new ClienteCartao();
            clienteCartao.setCartao(cartao);
            clienteCartao.setCpf(dados.getCpf());
            clienteCartao.setLimite(dados.getLimiteLiberado());
            clienteCartaoRepository.save(clienteCartao);
            log.info("Cartão cadastrado: {}", clienteCartao.toString());

        } catch (JsonProcessingException e) {
            log.error("Erro ao receber a solicitação da emissão do cartão: {}", e.getMessage());
        }
    }
}
