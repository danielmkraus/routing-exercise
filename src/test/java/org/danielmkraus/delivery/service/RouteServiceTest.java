package org.danielmkraus.delivery.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;

import javax.validation.ConstraintViolationException;

import org.danielmkraus.delivery.TestBase;
import org.danielmkraus.delivery.domain.Route;
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
public class RouteServiceTest extends BaseServiceTest{

	private static final double SAMPLE_TIME = 10d;

	private static final double SAMPLE_COST = 20d;

	@Autowired
	PointService pointService;
	
	@Autowired
	RouteService routeService;

	private Long pointAId;

	private Long pointBId;
	
	@Before
	public void setup() {
		pointAId = pointService.create(PointData.newInstance("A"));
		pointBId = pointService.create(PointData.newInstance("B"));
	}
	
	@Test
	public void should_create_new_route() {
		Long routeId = routeService.create(RouteData.builder().cost(SAMPLE_COST).time(SAMPLE_TIME).originId(pointAId).destinationId(pointBId).build());
		Route route = routeService.findById(routeId);
		assertThat(route).isNotNull().extracting(
				Route::getId, 
				Route::getCost, 
				Route::getTime, 
				(r)->r.getOrigin().getId(), 
				(r)->r.getDestination().getId())
		.contains(
					routeId, 
					SAMPLE_COST, 
					SAMPLE_TIME, 
					pointAId, 
					pointBId);
	}

	@Test(expected=ConstraintViolationException.class)
	public void should_fail_when_dont_fill_cost() {
		routeService.create(
				RouteData.builder().cost(null).time(SAMPLE_TIME).originId(pointAId).destinationId(pointBId).build());
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void should_fail_when_dont_fill_time() {
		routeService.create(
				RouteData.builder().cost(SAMPLE_COST).time(null).originId(pointAId).destinationId(pointBId).build());
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void should_fail_when_dont_fill_origin_id() {
		routeService.create(
				RouteData.builder().cost(SAMPLE_COST).time(SAMPLE_TIME).originId(null).destinationId(pointBId).build());
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void should_fail_when_dont_fill_destination_id() {
		routeService.create(
				RouteData.builder().cost(SAMPLE_COST).time(SAMPLE_TIME).originId(pointAId).destinationId(null).build());
	}
	

	@Test(expected=NoSuchElementException.class)
	public void should_fail_when_fill_unexistent_destination_id() {
		routeService.create(
				RouteData.builder().cost(SAMPLE_COST).time(SAMPLE_TIME).originId(pointAId).destinationId(TestBase.UNEXISTENT_ID).build());
	}
	

	@Test(expected=NoSuchElementException.class)
	public void should_fail_when_fill_unexistent_origin_id() {
		routeService.create(
				RouteData.builder().cost(SAMPLE_COST).time(SAMPLE_TIME).originId(TestBase.UNEXISTENT_ID).destinationId(pointBId).build());
	}
	
	@Test
	public void should_update_route() {
		Long routeId = routeService.create(RouteData.builder().cost(SAMPLE_COST).time(SAMPLE_TIME).originId(pointAId).destinationId(pointBId).build());
		
		routeService.update(routeId, RouteData.builder().cost(SAMPLE_COST).time(SAMPLE_TIME).originId(pointBId).destinationId(pointAId).build());
		
		Route route = routeService.findById(routeId);
		assertThat(route).isNotNull().extracting(
				Route::getId, 
				Route::getCost, 
				Route::getTime, 
				(r)->r.getOrigin().getId(), 
				(r)->r.getDestination().getId())
		.contains(
					routeId, 
					SAMPLE_COST, 
					SAMPLE_TIME, 
					pointBId, 
					pointAId);
	}
	
	

	@Test
	public void should_delete_route() {
		Long routeId = routeService.create(RouteData.builder().cost(SAMPLE_COST).time(SAMPLE_TIME).originId(pointAId).destinationId(pointBId).build());
		routeService.delete(routeId);
		
		assertThatThrownBy(()->routeService.findById(routeId))
			.isExactlyInstanceOf(NoSuchElementException.class);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void should_fail_when_update_with_null_id() {
		routeService.update(null, RouteData.builder().cost(SAMPLE_COST).time(SAMPLE_TIME).originId(pointBId).destinationId(pointAId).build());
	}

	@Test(expected=NoSuchElementException.class)
	public void should_fail_when_update_with_unexistent_id() {
		routeService.update(TestBase.UNEXISTENT_ID, RouteData.builder().cost(SAMPLE_COST).time(SAMPLE_TIME).originId(pointBId).destinationId(pointAId).build());
	}
	
	@Test(expected=NoSuchElementException.class)
	public void should_fail_when_delete_with_unexistent_id() {
		routeService.delete(TestBase.UNEXISTENT_ID);
	}

	@Test(expected=IllegalArgumentException.class)
	public void should_fail_when_delete_with_null_id() {
		routeService.delete(null);
	}
}
