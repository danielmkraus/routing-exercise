package org.danielmkraus.delivery.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.groups.Default;

import org.danielmkraus.delivery.domain.Point;
import org.danielmkraus.delivery.domain.Route;
import org.danielmkraus.delivery.domain.Route.RouteBuilder;
import org.danielmkraus.delivery.dto.RouteData;
import org.danielmkraus.delivery.repository.RouteRepository;
import org.danielmkraus.delivery.validation.Create;
import org.danielmkraus.delivery.validation.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
public class RouteServiceImpl implements RouteService {

	private static final String CSV_LINE_MATCHER = "^[\t ]*(.+)[\t ]*,[\t ]*(.+)[\t ]*,[\t ]*(\\d+.?\\d*)[\t ]*,[\t ]*(\\d+.?\\d*).*$";
	private RouteRepository routeRepository;
	private PointService pointService;
	private ValidationService validationService;
	
	/**
	 * Create a new instance of route service
	 * 
	 * @param repository
	 * @param pointService
	 * @param validationService
	 */
	public RouteServiceImpl(
			@Autowired RouteRepository repository, 
			@Autowired PointService pointService, 
			@Autowired ValidationService validationService) {
		this.validationService = validationService;
		this.routeRepository = repository;
		this.pointService = pointService;
	}

	private Route save(Route route) {
		return routeRepository.save(route, 0);
	}

	@Override 
	public void delete(Long id) {
		routeRepository.delete(findById(id));
	}

	@Override 
	public Long create(RouteData routeData) {
		Route route = createBuilderFromData(routeData).build();
		validationService.validate(route, Create.class, Default.class);
		return save(route).getId();	
	}

	@Override 
	public void update(Long id, RouteData routeData) {
		findById(id);
		Route route = createBuilderFromData(routeData).id(id).build();
		validationService.validate(route, Update.class, Default.class);
		save(route);
	}

	@Override 
	public Route findById(Long id) {
		return routeRepository.findById(id)
				.orElseThrow(()->new NoSuchElementException(String.format("Route with id %d not found.",id)));
	}
	
	@Override
	public void loadDataFromCSV(InputStream csvStream) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvStream))){
			reader.lines().map(this::processCSVLine).forEach(routeRepository::save);
		}catch(IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public Route processCSVLine(String line) {
		Pattern pattern = Pattern.compile(CSV_LINE_MATCHER);
		Matcher matcher = pattern.matcher(line);
		if(matcher.find()) {
			Point origin = pointService.findOrCreate(matcher.group(1));
			Point destination = pointService.findOrCreate(matcher.group(2));
			Double time = Double.parseDouble(matcher.group(3));
			Double cost = Double.parseDouble(matcher.group(4));
			return Route.builder().cost(cost).time(time).origin(origin).destination(destination).build();
		} else {
			throw new IllegalStateException(String.format("CSV line \"%s\" is not well formed.", line));
		}
	}

	private Point getPoint(Long pointId) {
		return pointId == null ? null : pointService.findById(pointId);
	}

	private RouteBuilder createBuilderFromData(RouteData routeData) {
		return Route.builder()
				.cost(routeData.getCost())
				.time(routeData.getTime())
				.origin(getPoint(routeData.getOriginId()))
				.destination(getPoint(routeData.getDestinationId()));
	}
}