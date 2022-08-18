package io.github.fintech.msavaliadorcredito.dto;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DadosSolicitacaoEmissaoCartao implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private Long idCartao;
	private String cpf;
	private String endereco;
	private BigDecimal limiteLiberado;
}
