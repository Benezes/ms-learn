package io.github.fintech.msavaliadorcredito.infra;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.fintech.msavaliadorcredito.dto.DadosSolicitacaoEmissaoCartao;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SolicitacaoEmissaoCartaoPublisher {

	private final RabbitTemplate rebbitTemplate;
	private final Queue queueEmissaoCartoes;
	private final ObjectMapper mapper;

	public void solicitarCartao(DadosSolicitacaoEmissaoCartao dadosSolicitacaoEmissaoCartao)
			throws JsonProcessingException {
		String json = convertIntoJson(dadosSolicitacaoEmissaoCartao);
		rebbitTemplate.convertAndSend(queueEmissaoCartoes.getName(), json);
	}

	private String convertIntoJson(DadosSolicitacaoEmissaoCartao dadosSolicitacaoEmissaoCartao)
			throws JsonProcessingException {
		return mapper.writeValueAsString(dadosSolicitacaoEmissaoCartao);
	}
}
