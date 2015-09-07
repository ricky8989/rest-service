package com.ricky.rest.messenger.exception;

public class DataNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -4980679817409543035L;
	
	public DataNotFoundException(String message){
		super(message);
	}
}
