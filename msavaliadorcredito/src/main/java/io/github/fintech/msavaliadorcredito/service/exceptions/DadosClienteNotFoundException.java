package io.github.fintech.msavaliadorcredito.service.exceptions;

public class DadosClienteNotFoundException extends Exception {

    public DadosClienteNotFoundException() {
        super("Dados do cliente não encontardo");
    }
}
