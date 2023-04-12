package com.at.exception;

public class CommonAbstractException extends RuntimeException {
	protected CommonAbstractException(String message, Throwable cause) {
		super(message, cause);
	}

	protected CommonAbstractException(String message) {
		super(message);
	}
}
