package org.danielmkraus.delivery.controller;

import org.danielmkraus.delivery.domain.Point;
import org.danielmkraus.delivery.dto.PointData;
import org.danielmkraus.delivery.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/points")
public class PointController {
	
	private PointService pointService;
	
	public PointController(@Autowired PointService pointService) {
		this.pointService = pointService;
	}
	
	@PostMapping
	public Long create(@RequestBody PointData pointData) {
		return pointService.create(pointData);
	}
	
	@PutMapping("/{id}")
	public void update(
			@PathVariable("id") Long id,
			@RequestBody PointData pointData) {
		pointService.update(id, pointData);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		pointService.delete(id);
	}

	@GetMapping("/{id}")
	public Point findById(@PathVariable("id") Long id) {
		return pointService.findById(id);
	}

	@GetMapping("/name/{name}")
	public Point findById(@PathVariable("name") String name) {
		return pointService.findByName(name);
	}
	
}
