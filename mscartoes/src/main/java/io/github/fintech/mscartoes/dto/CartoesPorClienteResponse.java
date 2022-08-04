package io.github.fintech.mscartoes.dto;

import io.github.fintech.mscartoes.domain.ClienteCartao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartoesPorClienteResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String nome;
    private String bandeira;
    private BigDecimal limiteLiberado;

    public static CartoesPorClienteResponse fromModel(ClienteCartao model) {
        return new CartoesPorClienteResponse(model.getCartao().getNome(), model.getCartao().getBandeira().toString(), model.getLimite());
    }
}
