package io.github.fintech.msclients.dto;

import io.github.fintech.msclients.domain.Cliente;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ClienteSaveRequeset implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String cpf;
    private String nome;
    private int idade;

    public Cliente toModel() {
        return new Cliente(cpf, nome, idade);
    }
}
