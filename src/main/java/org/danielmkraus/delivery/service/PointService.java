package org.danielmkraus.delivery.service;

import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.danielmkraus.delivery.domain.Point;
import org.danielmkraus.delivery.dto.PointData;

/**
 * Service interface to handle {@link Point} entity
 *
 */
public interface PointService {

	/**
	 * Create a new {@link Point}
	 * @param data {@link Point} information to persist
	 * @return created point id
	 */
	Long create(@Valid PointData data);

	/**
	 * update a {@link Point} with determined id
	 * @param id {@link Point} id to edit
	 * @param data {@link Point} information to update
	 */
	void update(Long id, @Valid PointData data);

	/**
	 * delete a {@link Point} by id
	 * @param id {@link Point} id to delete
	 */
	void delete(Long id);

	/**
	 * Retrieve a {@link Point} by id
	 * @param id {@link Point} id to retrieve
	 * @return {@link Point} with id
	 * @throws NoSuchElementException if don't exists a {@link Point} with determined id
	 */
	Point findById(Long id);

	/**
	 * Retrieve a {@link Point} by name
	 * @param name {@link Point} name to retrieve
	 * @return {@link Point} with name
	 * @throws NoSuchElementException if don't exists a {@link Point} with determined name
	 */
	Point findByName(String name);

	/**
	 * Retrieve a {@link Point} by name if exists, if don't, create a new point
	 * 
	 * @param name {@link Point} name to retrieve or create if not exists
	 * @return {@link Point} with name
	 */
	Point findOrCreate(String name);
}