package com.at.exception;

public class RestClientException extends CommonAbstractException {
	public RestClientException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public RestClientException(final String message) {
		super(message);
	}
}