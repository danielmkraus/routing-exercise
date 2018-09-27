package org.danielmkraus.delivery.service;

import java.awt.Point;
import java.io.InputStream;
import java.util.NoSuchElementException;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.danielmkraus.delivery.domain.Route;
import org.danielmkraus.delivery.dto.RouteData;
import org.danielmkraus.delivery.repository.RouteRepository;


/**
 * Service to manage routes, the business object for this service is {@link Route} 
 * 
 * @see RouteRepository 
 * @see Route
 * @see RouteData
 */
public interface RouteService {

	/**
	 * create a new route
	 * @param routeData
	 * @return created route id
	 * @throws NoSuchElementException if don't exist {@link Point} with id equal to {@link RouteData#getOriginId()} 
	 * 				or {@link RouteData#getDestinationId()}.
	 * @throws ConstraintViolationException if constraint violations occurs.
	 */
	Long create(@Valid RouteData routeData);
	
	/**
	 * update a existing route
	 * @param id route id to update
	 * @param routeData data to update route
	 * @throws NoSuchElementException if don't exist {@link Point} with id equal to {@link RouteData#getOriginId()} 
	 * 				or {@link RouteData#getDestinationId()}.
	 * @throws ConstraintViolationException if constraint violations occurs.
	 */
	void update(Long id, @Valid RouteData routeData);

	/**
	 * Delete a route by id
	 * @param id
	 */
	void delete(Long id);

	/**
	 * find a route by route id
	 * @param id route id to find
	 * @return route with corresponding id
	 * @throws NoSuchElementException if route with id parameter
	 */
	Route findById(Long id);

	/**
	 * create routes and points by a CSV file
	 * CSV file needs to have following format:
	 * 
	 * ORIGIN_NAME,DESTINATION_NAME,CostNumber,TimeNumber
	 * 
	 * @param csvStream
	 */
	void loadDataFromCSV(InputStream csvStream);

}