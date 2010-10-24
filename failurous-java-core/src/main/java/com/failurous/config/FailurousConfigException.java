package com.failurous.config;

public class FailurousConfigException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public FailurousConfigException(String message) {
		super(message);
	}
	
	public FailurousConfigException(String message, Throwable cause) {
		super(message, cause);
	}

}
