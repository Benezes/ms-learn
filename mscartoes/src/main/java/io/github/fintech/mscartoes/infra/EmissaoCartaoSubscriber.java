package io.github.fintech.mscartoes.infra;

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

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}