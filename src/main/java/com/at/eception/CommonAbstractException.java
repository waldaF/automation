package com.at.eception;

public class CommonAbstractException extends RuntimeException {
	protected CommonAbstractException(String message, Throwable cause) {
		super(message, cause);
	}
}
