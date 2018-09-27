package org.danielmkraus.delivery.repository;

import java.util.Optional;

import org.danielmkraus.delivery.domain.Point;
import org.springframework.data.neo4j.repository.Neo4jRepository;


public interface PointRepository extends Neo4jRepository<Point, Long>, PathRepository {
	Optional<Point> findByName(String name);
}
