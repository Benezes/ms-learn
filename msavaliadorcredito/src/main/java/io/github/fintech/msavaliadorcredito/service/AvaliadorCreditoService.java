package io.github.fintech.msavaliadorcredito.service;

import feign.FeignException;
import io.github.fintech.msavaliadorcredito.dto.*;
import io.github.fintech.msavaliadorcredito.repository.feign.CartoesResourceClient;
import io.github.fintech.msavaliadorcredito.repository.feign.ClienteResourceClient;
import io.github.fintech.msavaliadorcredito.service.exceptions.DadosClienteNotFoundException;
import io.github.fintech.msavaliadorcredito.service.exceptions.ErroComunicacaoMicroservicesException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clientesClientFeign;
    private final CartoesResourceClient cartoesClientFeign;

    // fazer uma chamada ao end points dos ms's afim de:
    // obter dados do cliente (ms-cliente)
    // obter cartoes do cliente (ms-cartoes)
    public SituacaoCliente obterSituacaoCliente(String cpf) throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException {
        ResponseEntity<DadosCliente> dadosClienteResponse = clientesClientFeign.dadosCliente(cpf);
        ResponseEntity<List<CartaoCliente>> cartoesClientesResponse = cartoesClientFeign.getCartoesByCliente(cpf);

        try {
            return SituacaoCliente.builder()
                    .cliente(dadosClienteResponse.getBody())
                    .cartoes(cartoesClientesResponse.getBody())
                    .build();
        } catch (FeignException.FeignClientException ex) {
            int status = ex.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroservicesException(ex.getMessage(), status);
        }
    }

    public RetornoAvaliacaoCliente realizarAvalicao(String cpf, Long renda) throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException {

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
}
