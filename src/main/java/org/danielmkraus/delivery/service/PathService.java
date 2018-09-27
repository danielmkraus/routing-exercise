package org.danielmkraus.delivery.service;

import java.util.NoSuchElementException;

import org.danielmkraus.delivery.domain.Point;
import org.danielmkraus.delivery.dto.Path;

/**
 * Service interface to find paths
 *
 */
public interface PathService {


	/**
	 * Find cheapest path without considering time
	 * @param sourceId source {@link Point} id to find a path
	 * @param targetId target {@link Point} id to find a path
	 * @return {@link Path} with lowest cost, without consider time
	 * @throws NoSuchElementException if don't find a path between sourceId and targetId
	 */
	default Path findCheapestPath(Long sourceId, Long targetId) {
		return findCheapestPath(sourceId, targetId, 0d);
	}

	Path findFastestPath(Long sourceId, Long targetId);

	/**
	 * Find cheapest path considering time cost, multiplying time with time 
	 * cost to get effective cost
	 * 
	 * @param sourceId source {@link Point} id to find a path
	 * @param targetId target {@link Point} id to find a path
	 * @param timeCost cost of time
	 * @return {@link Path} with lowest cost, considering time
	 * @throws NoSuchElementException if don't find a path between sourceId and targetId 
	 */
	Path findCheapestPath(Long sourceId, Long targetId, Double timeCost);
}
