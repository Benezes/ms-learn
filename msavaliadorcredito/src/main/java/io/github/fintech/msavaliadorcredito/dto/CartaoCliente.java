package io.github.fintech.msavaliadorcredito.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CartaoCliente implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String nome;
    private String bandeira;
    private BigDecimal limiteLiberado;
}
