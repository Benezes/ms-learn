package io.github.fintech.msavaliadorcredito.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RetornoAvaliacaoCliente implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private List<CartaoAprovado> cartoes;
}
