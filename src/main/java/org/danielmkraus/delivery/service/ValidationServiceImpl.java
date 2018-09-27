package org.danielmkraus.delivery.service;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class ValidationServiceImpl implements ValidationService {

	private Validator validator; 
	
	public ValidationServiceImpl(@Autowired Validator validator) {
		this.validator = validator;
	}
	
	@Override
	public <T> void validate(T toValidate, Class<?>... groups) {
		Set<ConstraintViolation<T>> constraintsViolated = 
				validator.validate(toValidate, groups);
		if( ! CollectionUtils.isEmpty(constraintsViolated)) {
			throw new ConstraintViolationException(constraintsViolated);
		}
	}
}
