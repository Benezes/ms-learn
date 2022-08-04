package io.github.fintech.msavaliadorcredito.service;

import feign.FeignException;
import io.github.fintech.msavaliadorcredito.dto.CartaoCliente;
import io.github.fintech.msavaliadorcredito.dto.DadosCliente;
import io.github.fintech.msavaliadorcredito.dto.SituacaoCliente;
import io.github.fintech.msavaliadorcredito.repository.feign.CartoesResourceClient;
import io.github.fintech.msavaliadorcredito.repository.feign.ClienteResourceClient;
import io.github.fintech.msavaliadorcredito.service.exceptions.DadosClienteNotFoundException;
import io.github.fintech.msavaliadorcredito.service.exceptions.ErroComunicacaoMicroservicesException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
