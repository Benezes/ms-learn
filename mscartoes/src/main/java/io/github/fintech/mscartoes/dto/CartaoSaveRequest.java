package io.github.fintech.mscartoes.dto;


import io.github.fintech.mscartoes.domain.BandeiraCartao;
import io.github.fintech.mscartoes.domain.Cartao;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CartaoSaveRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String nome;
    private BandeiraCartao bandeira;
    private BigDecimal renda;
    private BigDecimal limite;

    public Cartao toModel() {
        return new Cartao(nome, bandeira, renda, limite);
    }
}
