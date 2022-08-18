package io.github.fintech.msavaliadorcredito.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import feign.FeignException;
import io.github.fintech.msavaliadorcredito.dto.Cartao;
import io.github.fintech.msavaliadorcredito.dto.CartaoAprovado;
import io.github.fintech.msavaliadorcredito.dto.CartaoCliente;
import io.github.fintech.msavaliadorcredito.dto.DadosCliente;
import io.github.fintech.msavaliadorcredito.dto.DadosSolicitacaoEmissaoCartao;
import io.github.fintech.msavaliadorcredito.dto.ProtocoloSolicitacaoCartao;
import io.github.fintech.msavaliadorcredito.dto.RetornoAvaliacaoCliente;
import io.github.fintech.msavaliadorcredito.dto.SituacaoCliente;
import io.github.fintech.msavaliadorcredito.infra.SolicitacaoEmissaoCartaoPublisher;
import io.github.fintech.msavaliadorcredito.repository.feign.CartoesResourceClient;
import io.github.fintech.msavaliadorcredito.repository.feign.ClienteResourceClient;
import io.github.fintech.msavaliadorcredito.service.exceptions.DadosClienteNotFoundException;
import io.github.fintech.msavaliadorcredito.service.exceptions.ErroComunicacaoMicroservicesException;
import io.github.fintech.msavaliadorcredito.service.exceptions.ErroSolicitacaoCartaoException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

	private final ClienteResourceClient clientesClientFeign;
	private final CartoesResourceClient cartoesClientFeign;
	private final SolicitacaoEmissaoCartaoPublisher solicitacaoEmissaoCartaoPublisher;

	// fazer uma chamada ao end points dos ms's afim de:
	// obter dados do cliente (ms-cliente)
	// obter cartoes do cliente (ms-cartoes)
	public SituacaoCliente obterSituacaoCliente(String cpf)
			throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException {
		ResponseEntity<DadosCliente> dadosClienteResponse = clientesClientFeign.dadosCliente(cpf);
		ResponseEntity<List<CartaoCliente>> cartoesClientesResponse = cartoesClientFeign.getCartoesByCliente(cpf);

		try {
			return SituacaoCliente.builder().cliente(dadosClienteResponse.getBody())
					.cartoes(cartoesClientesResponse.getBody()).build();
		} catch (FeignException.FeignClientException ex) {
			int status = ex.status();
			if (HttpStatus.NOT_FOUND.value() == status) {
				throw new DadosClienteNotFoundException();
			}
			throw new ErroComunicacaoMicroservicesException(ex.getMessage(), status);
		}
	}

	public RetornoAvaliacaoCliente realizarAvalicao(String cpf, Long renda)
			throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException {

		try {
			ResponseEntity<DadosCliente> dadosClienteResponse = clientesClientFeign.dadosCliente(cpf);
			ResponseEntity<List<Cartao>> cartoesResponse = cartoesClientFeign.getCartoesRendaAte(renda);

			List<Cartao> cartoes = cartoesResponse.getBody();
			List<CartaoAprovado> listaCartoesAprovados = cartoes.stream().map(cartao -> {
				DadosCliente dadosCliente = dadosClienteResponse.getBody();

				BigDecimal limiteBasico = cartao.getLimiteBasico();
				BigDecimal idadeBD = BigDecimal.valueOf(dadosCliente.getIdade());
				BigDecimal fator = idadeBD.divide(BigDecimal.valueOf(10));
				BigDecimal limiteAprovado = fator.multiply(limiteBasico);

				CartaoAprovado aprovado = new CartaoAprovado();
				aprovado.setCartao(cartao.getNome());
				aprovado.setBandeira(cartao.getBandeira());
				aprovado.setLimiteAprovado(limiteAprovado);

				return aprovado;
			}).collect(Collectors.toList());

			return new RetornoAvaliacaoCliente(listaCartoesAprovados);

		} catch (FeignException.FeignClientException ex) {
			int status = ex.status();
			if (HttpStatus.NOT_FOUND.value() == status) {
				throw new DadosClienteNotFoundException();
			}
			throw new ErroComunicacaoMicroservicesException(ex.getMessage(), status);
		}
	}

	public ProtocoloSolicitacaoCartao solicitarEmissaoCartao(
			DadosSolicitacaoEmissaoCartao dadosSolicitacaoEmissaoCartao) {
		try {
			solicitacaoEmissaoCartaoPublisher.solicitarCartao(dadosSolicitacaoEmissaoCartao);
			String protocolo = UUID.randomUUID().toString();
			return new ProtocoloSolicitacaoCartao(protocolo);
		} catch (Exception e) {
			throw new ErroSolicitacaoCartaoException(e.getMessage());
		}
	}
}
