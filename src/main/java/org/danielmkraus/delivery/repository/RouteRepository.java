package org.danielmkraus.delivery.repository;

import org.danielmkraus.delivery.domain.Route;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface RouteRepository extends Neo4jRepository<Route, Long>{
}
