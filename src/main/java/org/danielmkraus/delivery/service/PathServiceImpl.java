package org.danielmkraus.delivery.service;

import org.danielmkraus.delivery.dto.Path;
import org.danielmkraus.delivery.repository.PathRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional(readOnly=true)
public class PathServiceImpl implements PathService {

	private PathRepository pathRepository;
	
	public PathServiceImpl(@Autowired PathRepository pathRepository) {
		this.pathRepository = pathRepository;
	}

	@Override
	public Path findFastestPath(Long sourceId, Long targetId) {
		return pathRepository.findFastestPath(sourceId, targetId)
				.get();
	}

	@Override
	public Path findCheapestPath(Long sourceId, Long targetId, Double timeCost) {
		return pathRepository.findCheapestPath(sourceId, targetId, timeCost)
				.get();
	}
}
