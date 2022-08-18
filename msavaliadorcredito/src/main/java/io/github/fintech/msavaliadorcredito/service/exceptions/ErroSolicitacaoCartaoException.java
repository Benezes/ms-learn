package io.github.fintech.msavaliadorcredito.service.exceptions;

public class ErroSolicitacaoCartaoException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ErroSolicitacaoCartaoException() {
		super();
	}

	public ErroSolicitacaoCartaoException(String message) {
		super(message);
	}

}
