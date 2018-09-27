package org.danielmkraus.delivery.service;

import java.util.Collection;

import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public class BaseServiceTest {

	@Autowired
	Collection<Neo4jRepository<?, ?>> repositories;
	
	@After
	public void clearDatabase() {
		repositories.forEach(Neo4jRepository::deleteAll);
	}
}
