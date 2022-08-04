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
@Builder
public class SituacaoCliente implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private DadosCliente cliente;
    private List<CartaoCliente> cartoes;
}
