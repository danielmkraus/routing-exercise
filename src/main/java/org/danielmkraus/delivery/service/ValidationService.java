package org.danielmkraus.delivery.service;

/**
 * Service to make validation with Bean Validation (JSR 303)
 *
 */
public interface ValidationService {

	/**
	 * Apply validation through bean validation
	 * 
	 * @param toValidate
	 *            object to be validated
	 * @param groups
	 *            validation groups to apply
	 */
	<X> void validate(X toValidate, Class<?>... groups);

}