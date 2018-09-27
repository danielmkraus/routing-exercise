package org.danielmkraus.delivery;

import java.util.NoSuchElementException;

import org.danielmkraus.delivery.domain.Point;
import org.danielmkraus.delivery.dto.RouteData;
import org.danielmkraus.delivery.service.PointService;
import org.mockito.Mockito;

public class TestBase {

	public static final String POINT_A_NAME = "A";
	public static final String POINT_B_NAME = "B";
	
	public static final long POINT_A_ID = 1;
	public static final long POINT_B_ID = 2;
	public static final long UNEXISTENT_ID = 9999999;
	
	public static final Point POINT_A = Point.builder().id(POINT_A_ID).name(POINT_A_NAME).build();
	public static final Point POINT_B = Point.builder().id(POINT_B_ID).name(POINT_B_NAME).build();
	

	public static RouteData sampleRouteData() {
		RouteData routeData = new RouteData();
		routeData.setCost(1d);
		routeData.setTime(1d);
		routeData.setOriginId(POINT_A_ID);
		routeData.setOriginId(POINT_B_ID);
		return routeData;
	}
	
	public static PointService mockPointService() {
		PointService pointServiceMock = Mockito.mock(PointService.class);
		Mockito.when(pointServiceMock.findById(POINT_A_ID)).thenReturn(POINT_A);
		Mockito.when(pointServiceMock.findById(POINT_B_ID)).thenReturn(POINT_B);
		Mockito.when(pointServiceMock.findById(UNEXISTENT_ID)).thenThrow(new NoSuchElementException());
		
		Mockito.when(pointServiceMock.findByName(POINT_A_NAME)).thenReturn(POINT_A);
		Mockito.when(pointServiceMock.findByName(POINT_B_NAME)).thenReturn(POINT_B);
		Mockito.when(pointServiceMock.findByName(Mockito.anyString())).thenThrow(new NoSuchElementException());
	
		return pointServiceMock;
	}
}
