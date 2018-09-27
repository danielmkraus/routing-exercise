package org.danielmkraus.delivery.controller;

import org.danielmkraus.delivery.dto.Path;
import org.danielmkraus.delivery.service.PathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/paths")
public class PathController {
	
	private PathService pathService;

	public PathController(@Autowired PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping("/fastest/source/{source}/target/{target}")
	public  Path findFastestPath(
			@PathVariable("source") Long sourceId, 
			@PathVariable("target") Long targetId) {
		return pathService.findFastestPath(sourceId, targetId);
	}
	
	@GetMapping("/cheapest/source/{source}/target/{target}")
	public Path findCheapestPath(
			@PathVariable("source") Long sourceId, 
			@PathVariable("target") Long targetId) {
		return pathService.findCheapestPath(sourceId, targetId);
	}
	
	@GetMapping("/cheapest/source/{source}/target/{target}/cost/{timeCost}")
	public Path findCheapestPath(
			@PathVariable("source") Long sourceId, 
			@PathVariable("target") Long targetId,
			@PathVariable("timeCost") Double timeCost) {
		return pathService.findCheapestPath(sourceId, targetId, timeCost);
	}
}
