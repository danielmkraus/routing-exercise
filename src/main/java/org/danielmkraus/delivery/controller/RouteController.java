package org.danielmkraus.delivery.controller;

import org.danielmkraus.delivery.domain.Route;
import org.danielmkraus.delivery.dto.RouteData;
import org.danielmkraus.delivery.service.RouteService;
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
@RequestMapping(path="/routes")
public class RouteController {

	private RouteService service;
	
	public RouteController(@Autowired RouteService service) {
		this.service = service;
	}
	
	@PostMapping
	public Long create(@RequestBody RouteData routeData) {
		return service.create(routeData);
	}
	
	@PutMapping("/{id}")
	public void update( 
			@PathVariable("id") Long id,
			@RequestBody RouteData routeData) {
		service.update(id, routeData);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		service.delete(id);
	}
	
	@GetMapping("/{id}")
	public Route findById(@PathVariable("id") Long id) {
		return service.findById(id);
	}
}
