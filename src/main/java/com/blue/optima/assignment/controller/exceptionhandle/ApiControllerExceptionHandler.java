package com.blue.optima.assignment.controller.exceptionhandle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.blue.optima.assignment.exception.BadHttpRequestException;
import com.blue.optima.assignment.exception.RecordNotFoundException;

@ControllerAdvice
public class ApiControllerExceptionHandler {
	
	 private static final Logger logger = LoggerFactory.getLogger(ApiControllerExceptionHandler.class);
	
	/**
	 * Handles all Exception
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(value = RecordNotFoundException.class)
	@ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ResponseBody
	public ResponseEntity<String> handleRecordNotFoundException(RecordNotFoundException exception) {
		logger.error("RecordNotFoundException thrown");
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
		
	}
	
	
	
	/**
	 * Handles all Exception
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(value = BadHttpRequestException.class)
	@ResponseStatus(value= HttpStatus.BAD_REQUEST)
    @ResponseBody
	public ResponseEntity<String> handleBadHttpRequestException(BadHttpRequestException exception) {
		logger.error("BadHttpRequestException thrown");
		
		return new ResponseEntity<>( exception.getMessage(), HttpStatus.BAD_REQUEST);
		
	}
	
	
	/**
	 * Handles all Exception
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
	public ResponseEntity<String> handleException(Exception exception) {
		logger.error("Generic Exception thrown");
		
		return new ResponseEntity<>( exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
}
