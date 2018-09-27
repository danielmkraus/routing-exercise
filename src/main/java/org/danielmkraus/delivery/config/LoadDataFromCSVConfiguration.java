package org.danielmkraus.delivery.config;

import javax.annotation.PostConstruct;

import org.danielmkraus.delivery.repository.PointRepository;
import org.danielmkraus.delivery.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;

@Configuration
public class LoadDataFromCSVConfiguration {

	@Value("${delivery.import.data.csv:false}")
	private boolean shouldImportData;
	
	private RouteService routeService;
	
	private PointRepository routeRepository;

	public LoadDataFromCSVConfiguration(@Autowired RouteService routeService, @Autowired PointRepository pointRepository) {
		this.routeService = routeService;
		this.routeRepository = pointRepository;
	}
	
	@PostConstruct
	public void importData() {
		boolean hasAnyPointRegistered = routeRepository.findAll(PageRequest.of(0, 1), 0).hasContent();
		if(hasAnyPointRegistered || !shouldImportData) {
			return;
		}
		routeService.loadDataFromCSV(this.getClass().getResourceAsStream("/routes.csv"));
	}
	
}
