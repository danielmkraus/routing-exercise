package org.danielmkraus.delivery.service;

import static org.danielmkraus.delivery.TestBase.UNEXISTENT_ID;
import static org.danielmkraus.delivery.TestBase.mockPointService;
import static org.danielmkraus.delivery.TestBase.sampleRouteData;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;

import org.danielmkraus.delivery.domain.Route;
import org.danielmkraus.delivery.dto.RouteData;
import org.danielmkraus.delivery.repository.RouteRepository;
import org.junit.Before;
import org.junit.Test;

public class RouteServiceImplUnitTest {

	private RouteService routeService;
	
	private RouteRepository routeRepositoryMock;
	
	private ValidationService validationServiceMock;
	
	@Before
	public void setup() {
		validationServiceMock = mock(ValidationService.class);
		routeRepositoryMock = mock(RouteRepository.class);
		when(routeRepositoryMock.save(any())).thenReturn(Route.builder().build());
		when(routeRepositoryMock.save((Route)any(), anyInt())).thenReturn(Route.builder().build());

		routeService = new RouteServiceImpl(routeRepositoryMock, mockPointService(), validationServiceMock);
	}

	@Test
	public void should_create_route() {
		routeService.create(sampleRouteData());
		verify(validationServiceMock, times(1)).validate(any(),any());
		verify(routeRepositoryMock, times(1)).save((Route)any(), anyInt());
	}
	
	@Test(expected=NoSuchElementException.class)
	public void fail_when_save_with_invalid_origin_point() {
		RouteData sampleRouteData = sampleRouteData();
		sampleRouteData.setOriginId(UNEXISTENT_ID);
		routeService.create(sampleRouteData);
	}
	
	@Test(expected=NoSuchElementException.class)
	public void fail_when_save_with_invalid_destination_point() {
		RouteData sampleRouteData = sampleRouteData();
		sampleRouteData.setDestinationId(UNEXISTENT_ID);
		routeService.create(sampleRouteData);
	}
	
	@Test(expected=IllegalStateException.class)
	public void fail_when_load_a_not_well_formed_csv() {
		routeService.loadDataFromCSV(new ByteArrayInputStream("ACME".getBytes()));
	}
	
	@Test
	public void should_parse_csv() throws IOException {
		routeService.loadDataFromCSV(new ByteArrayInputStream("A,B,1,1\n,B,A,2.,2.".getBytes()));
		verify(routeRepositoryMock, times(2)).save((Route)any());
	}
	
}
