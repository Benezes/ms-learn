package io.github.fintech.msavaliadorcredito.dto;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProtocoloSolicitacaoCartao implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private String protocolo;

}
