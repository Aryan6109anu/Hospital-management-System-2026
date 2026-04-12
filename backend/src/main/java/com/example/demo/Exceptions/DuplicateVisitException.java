package com.example.demo.Exceptions;

public class DuplicateVisitException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateVisitException(String message) {
        super(message);
    }
}
