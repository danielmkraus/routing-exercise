package org.danielmkraus.delivery.service;

import static org.assertj.core.api.Assertions.*;

import java.util.NoSuchElementException;

import javax.validation.ConstraintViolationException;

import org.danielmkraus.delivery.TestBase;
import org.danielmkraus.delivery.domain.Point;
import org.danielmkraus.delivery.dto.PointData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PointServiceTest extends BaseServiceTest{

	private static final String OTHER_SAMPLE_NAME = "X";

	private static final String SAMPLE_POINT_NAME = "A";
	
	@Autowired
	PointService pointService;
	
	@Test(expected=NullPointerException.class)
	public void fail_when_create_a_null_point() {
		pointService.create(null);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void fail_when_create_a_point_without_name() {
		pointService.create(new PointData());
	}
	
	@Test
	public void should_create_new_point() {
		Long createdPointId = pointService.create(samplePointData());
		
		assertThat(createdPointId).isNotNull();
		Point point = pointService.findById(createdPointId);
		assertThat(point).isNotNull()
			.extracting(Point::getName, Point::getId)
			.contains(SAMPLE_POINT_NAME, createdPointId);
	}
	

	@Test
	public void should_update_point() {
		Long createdPointId = pointService.create(samplePointData());
		assertThat(createdPointId).isNotNull();

		PointData p = samplePointData();
		p.setName(OTHER_SAMPLE_NAME);
		pointService.update(createdPointId, p);
		
		Point point = pointService.findById(createdPointId);
		assertThat(point).isNotNull()
			.extracting(Point::getName, Point::getId)
			.contains(OTHER_SAMPLE_NAME, createdPointId);
	}
	

	@Test(expected=DataIntegrityViolationException.class)
	public void should_fail_when_update_a_point_with_a_existing_name() {
		PointData p = samplePointData();
		Long idToEdit = pointService.create(p);
		
		p.setName(OTHER_SAMPLE_NAME);
		pointService.create(p);
		
		pointService.update(idToEdit, p);
	}
	

	@Test(expected=ConstraintViolationException.class)
	public void should_fail_when_update_a_point_without_name() {
		PointData p = samplePointData();
		Long idToEdit = pointService.create(p);
		
		p.setName(null);
		pointService.create(p);
		
		pointService.update(idToEdit, p);
	}

	@Test(expected=IllegalArgumentException.class)
	public void should_fail_when_update_a_point_without_id() {
		pointService.update(null, samplePointData());
	}
	

	@Test(expected=DataIntegrityViolationException.class)
	public void should_fail_when_create_two_points_with_same_name() {
		PointData p = samplePointData();
		pointService.create(p);
		pointService.create(p);
	}
	
	@Test
	public void should_delete_point() {
		PointData p = samplePointData();
		final Long idToDelete = pointService.create(p);
		
		pointService.delete(idToDelete);
		
		assertThatThrownBy(()->pointService.findById(idToDelete))
			.isExactlyInstanceOf(NoSuchElementException.class);
	}

	@Test(expected=NoSuchElementException.class)
	public void should_fail_when_delete_a_unexistent_point() {
		pointService.delete(TestBase.UNEXISTENT_ID);
	}
	
	@Test
	public void should_find_a_point_by_name() {
		Long createdPointId = pointService.create(samplePointData());
		
		assertThat(createdPointId).isNotNull();
		Point point = pointService.findByName(SAMPLE_POINT_NAME);
		assertThat(point).isNotNull()
			.extracting(Point::getName, Point::getId)
			.contains(SAMPLE_POINT_NAME, createdPointId);
	}

	@Test(expected=NoSuchElementException.class)
	public void should_fail_when_find_by_a_unexistent_point_name() {
		pointService.findByName("UNEXISTENT");
	}

	@Test(expected=NoSuchElementException.class)
	public void should_fail_when_find_by_a_unexistent_point_id() {
		pointService.findById(TestBase.UNEXISTENT_ID);
	}
	
	@Test
	public void should_create_and_then_retrieve_point() {
		Point point = pointService.findOrCreate("XPTO");
		Point point2 = pointService.findOrCreate("XPTO");
		
		assertThat(point.getId()).isEqualByComparingTo(point2.getId());
	}
	
	private PointData samplePointData() {
		PointData p = new PointData();
		p.setName(SAMPLE_POINT_NAME);
		return p;
	}
	

}
