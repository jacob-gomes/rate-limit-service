package com.blue.optima.assignment.exception;

public class RecordNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 317923320503903129L;

	public RecordNotFoundException() {
		super("Record Not Found.");
	}
}
