package org.danielmkraus.delivery.dto;

import java.util.List;

import org.danielmkraus.delivery.domain.Point;
import org.springframework.data.neo4j.annotation.QueryResult;

import lombok.Data;


@Data
@QueryResult
public class Path {
	
	private List<Point> nodes;
	
	private Double totalCost;

	private Double totalTime;
}

