package org.danielmkraus.delivery.repository;

import java.util.Optional;

import org.danielmkraus.delivery.dto.Path;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;

public interface PathRepository {

	@Query("MATCH  shortestPath=(startNode:Point)-[:GO_TO*]->(endNode:Point)\n" +
		    "WHERE id(startNode)={sourcePointId} AND id(endNode)={targetPointId} \n" + 
			"RETURN nodes(shortestPath) AS nodes,\n" + 
			"                reduce(cost=0, r in relationships(shortestPath) |  cost+r.cost) AS totalCost,\n" + 
			"                reduce(time=0, r in relationships(shortestPath) |  time+r.time) AS totalTime\n" + 
			"                ORDER BY totalTime ASC\n" + 
			"                LIMIT 1;")
	public Optional<Path> findFastestPath(
			@Param("sourcePointId") Long sourcePointId, 
			@Param("targetPointId") Long targetPointId);
	
	@Query("MATCH  shortestPath=(startNode:Point)-[:GO_TO*]->(endNode:Point)\n" +
		    "WHERE id(startNode)={sourcePointId} AND id(endNode)={targetPointId} \n" + 
			"RETURN nodes(shortestPath) AS nodes,\n" + 
			"                reduce(cost=0, r in relationships(shortestPath) |  cost+r.cost+r.time*{timeCost}) AS totalCost,\n" + 
			"                reduce(time=0, r in relationships(shortestPath) |  time+r.time) AS totalTime\n" + 
			"                ORDER BY totalCost ASC\n" + 
			"                LIMIT 1;")
	public Optional<Path> findCheapestPath(
			@Param("sourcePointId") Long sourcePointId, 
			@Param("targetPointId") Long targetPointId, 
			@Param("timeCost") Double timeCost);
}
