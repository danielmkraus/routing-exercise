package org.danielmkraus.delivery.exception;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ControllerAdvice
public class RestResponseEntityExceptionHandler 
  extends ResponseEntityExceptionHandler {
 
	private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException e, WebRequest request){
    	LOGGER.error(e.getMessage(), e);
	    String constraintViolations =	
	    	e.getConstraintViolations()
	    		.stream()
		    		.map(constraintViolation->constraintViolation.getMessage())
		    		.collect(Collectors.joining("\\n"));
    	return ResponseEntity.badRequest().body(ErrorMessage.newInstance(constraintViolations));
    }
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDatabaseConstraintViolation(DataIntegrityViolationException e, WebRequest request){
    	LOGGER.error(e.getMessage(), e);
    	return ResponseEntity.badRequest().body(ErrorMessage.newInstance(e.getMessage()));
    }
    
    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<Object> handleConstraintViolation(NoSuchElementException e, WebRequest request) {
    	LOGGER.error(e.getMessage(), e);
    	return ResponseEntity.notFound().build();
    }
    
    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<Object> handleIllegalStateException(IllegalStateException e, WebRequest request) {
    	LOGGER.error(e.getMessage(), e);
    	return ResponseEntity.badRequest().body(ErrorMessage.newInstance(e.getMessage()));
    }
    
    @NoArgsConstructor
    @AllArgsConstructor(staticName="newInstance")
    public static final class ErrorMessage{
    	@Getter private String message;
    }
}