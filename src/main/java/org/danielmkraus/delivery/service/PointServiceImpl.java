package org.danielmkraus.delivery.service;

import javax.validation.Valid;
import javax.validation.groups.Default;

import org.danielmkraus.delivery.domain.Point;
import org.danielmkraus.delivery.domain.Point.PointBuilder;
import org.danielmkraus.delivery.dto.PointData;
import org.danielmkraus.delivery.repository.PointRepository;
import org.danielmkraus.delivery.validation.Create;
import org.danielmkraus.delivery.validation.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
public class PointServiceImpl implements PointService {

	private PointRepository pointRepository;
	private ValidationService validationService;
	
	public PointServiceImpl(
			@Autowired PointRepository repository, 
			@Autowired ValidationService validationService) {
		this.pointRepository = repository;
		this.validationService = validationService;
	}
	
	@Override 
	public Long create(@Valid PointData pointData) {
		Point point = createBuilderFromData(pointData).build();
		validationService.validate(point, Create.class, Default.class);
		return save(point).getId();
	}
	
	@Override 
	public void update(Long id, @Valid PointData pointData) {
		findById(id);
		Point point = createBuilderFromData(pointData).id(id).build();
		validationService.validate(point, Update.class, Default.class);
		save(point);
	}

	@Override 
	public void delete(Long id) {
		pointRepository.delete(findById(id));
	}

	@Override 
	public Point findById(Long id) {
		return pointRepository.findById(id, 0).get();
	}
	
	@Override 
	public Point findByName(String name) {
		return pointRepository.findByName(name).get();
	}

	public Point save(Point point) {
		return pointRepository.save(point,0);
	}
	
	private PointBuilder createBuilderFromData(PointData pointData) {
		return Point.builder().name(pointData.getName());
	}

	@Override
	public Point findOrCreate(String name) {
		return pointRepository.findByName(name)
				.orElseGet(()->
						pointRepository.save(Point.builder().name(name).build())
				);
	}
}
