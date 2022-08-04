package io.github.fintech.msavaliadorcredito.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DadosAvaliacao implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String cpf;
    private Long renda;
}
