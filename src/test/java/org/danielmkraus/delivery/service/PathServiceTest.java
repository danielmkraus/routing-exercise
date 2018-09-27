package org.danielmkraus.delivery.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.NoSuchElementException;

import org.danielmkraus.delivery.domain.Point;
import org.danielmkraus.delivery.dto.Path;
import org.danielmkraus.delivery.dto.PointData;
import org.danielmkraus.delivery.dto.RouteData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PathServiceTest extends BaseServiceTest{

	@Autowired
	PathService pathService;
	
	@Autowired
	RouteService routeService;
	
	@Autowired
	PointService pointService;
	
	private Long pointAId;

	private Long pointDId;
	
	@Before
	public void setup() {
		pointAId = pointService.create(PointData.newInstance("A"));
		Long pointBId = pointService.create(PointData.newInstance("B"));
		Long pointCId = pointService.create(PointData.newInstance("C"));
		pointDId = pointService.create(PointData.newInstance("D"));
		
		routeService.create(RouteData.builder().cost(1d).time(10d).originId(pointAId).destinationId(pointBId).build());
		routeService.create(RouteData.builder().cost(1d).time(10d).originId(pointBId).destinationId(pointCId).build());
		routeService.create(RouteData.builder().cost(1d).time(10d).originId(pointCId).destinationId(pointDId).build());
		routeService.create(RouteData.builder().cost(10d).time(1d).originId(pointAId).destinationId(pointDId).build());
	}
	
	@Test
	public void should_find_cheapest_path() {
		Path p = pathService.findCheapestPath(pointAId, pointDId);
		assertThat(p).isNotNull();
		assertThat(p.getNodes()).extracting(Point::getName).contains("A", "B", "C");
		assertThat(p.getTotalCost()).isEqualByComparingTo(3d);
		assertThat(p.getTotalTime()).isEqualByComparingTo(30d);
	}
	
	@Test
	public void should_find_fastest_path() {
		Path p = pathService.findFastestPath(pointAId, pointDId);
		assertThat(p).isNotNull();
		assertThat(p.getNodes()).extracting(Point::getName).contains("A", "D");
		assertThat(p.getTotalCost()).isEqualByComparingTo(10d);
		assertThat(p.getTotalTime()).isEqualByComparingTo(1d);
	}
	
	@Test
	public void should_find_cheapest_path_with_time_cost() {
		Path p = pathService.findCheapestPath(pointAId, pointDId, 100d);
		assertThat(p).isNotNull();
		assertThat(p.getNodes()).extracting(Point::getName).contains("A", "D");
		assertThat(p.getTotalCost()).isEqualByComparingTo(110d);
		assertThat(p.getTotalTime()).isEqualByComparingTo(1d);
	}

	@Test(expected=NoSuchElementException.class)
	public void should_not_find_unexistent_cheapest_path_with_time_cost() {
		pathService.findCheapestPath(pointDId, pointAId, 100d);
	}

	@Test(expected=NoSuchElementException.class)
	public void should_not_find_unexistent_cheapest_path() {
		pathService.findCheapestPath(pointDId, pointAId);
	}

	@Test(expected=NoSuchElementException.class)
	public void should_not_find_unexistent_fastest_path() {
		pathService.findFastestPath(pointDId, pointAId);
	}
}
