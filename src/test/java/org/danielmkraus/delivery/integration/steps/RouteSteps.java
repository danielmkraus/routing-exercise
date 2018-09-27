package org.danielmkraus.delivery.integration.steps;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.danielmkraus.delivery.domain.Point;
import org.danielmkraus.delivery.domain.Route;
import org.danielmkraus.delivery.dto.Path;
import org.danielmkraus.delivery.dto.RouteData;
import org.danielmkraus.delivery.integration.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class RouteSteps extends SpringIntegrationTest {

	@Autowired
	TestRestTemplate testRestTemplate;

	@Autowired
	Collection<Neo4jRepository<?, ?>> repositories;

	private ResponseEntity<?> response;

	private Long createdRouteId;

	@After
	public void tearDown() {
		repositories.forEach(Neo4jRepository::deleteAll);
	}

	@When("^register these routes:$")
	public void register_these_routes(List<RouteTestData> routesData) throws Throwable {
		routesData.forEach(routeTestData -> {
			ResponseEntity<?> response = saveRoute(routeTestData);
			assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
		});
	}

	@Then("^the routes will be successfully registered$")
	public void the_routes_will_be_successfully_registered() throws Throwable {
	}

	@Then("^the cheapest path from (\\w+) to (\\w+) has time (\\d+) and cost (\\d+) with this path:$")
	public void the_cheapest_path_from_to_has_time_and_cost_with_this_path(String origin, String destination,
			double time, double cost, List<Point> nodes) throws Throwable {
		Long originId = getPointByName(origin).getId();
		Long destinationId = getPointByName(destination).getId();
		ResponseEntity<Path> response = testRestTemplate
				.getForEntity("/paths/cheapest/source/" + originId + "/target/" + destinationId, Path.class);
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);

		Path path = response.getBody();
		assertThat(path.getNodes()).extracting(Point::getName)
				.containsAll(nodes.stream().map(Point::getName).collect(Collectors.toList()));
	}

	@Then("^the fastest path from (\\w+) to (\\w+) has time (\\d+) and cost (\\d+) with this path:$")
	public void the_fastest_path_from_to_has_time_and_cost_with_this_path(String origin, String destination,
			double time, double cost, List<Point> nodes) throws Throwable {

		Long originId = getPointByName(origin).getId();
		Long destinationId = getPointByName(destination).getId();
		ResponseEntity<Path> response = testRestTemplate
				.getForEntity("/paths/fastest/source/" + originId + "/target/" + destinationId, Path.class);
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);

		Path path = response.getBody();
		assertThat(path.getNodes()).extracting(Point::getName)
				.containsAll(nodes.stream().map(Point::getName).collect(Collectors.toList()));
	}

	@Then("^the cheapest path when time cost is (\\d+) from (\\w+) to (\\w+) has time (\\d+) and cost (\\d+) with this path:$")
	public void the_cheapest_path_when_time_cost_is_from_A_to_D_has_time_and_cost_with_this_path(double timeCost,
			String origin, String destination, double time, double cost, List<Point> nodes) throws Throwable {
		Long originId = getPointByName(origin).getId();
		Long destinationId = getPointByName(destination).getId();
		ResponseEntity<Path> response = testRestTemplate.getForEntity(
				"/paths/cheapest/source/" + originId + "/target/" + destinationId + "/cost/" + timeCost, Path.class);
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);

		Path path = response.getBody();
		assertThat(path.getNodes()).extracting(Point::getName)
				.containsAll(nodes.stream().map(Point::getName).collect(Collectors.toList()));
	}

	@When("^register a route without time$")
	public void register_a_route_without_time() throws Throwable {
		RouteTestData sample = RouteTestData.sample();
		sample.time = null;
		response = saveRoute(sample);
	}

	@Then("^will fail in route register with message \"([^\"]*)\"$")
	public void will_fail_in_route_register_with_message(String message) throws Throwable {
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody().toString()).contains(message);
	}

	@When("^register a route without cost$")
	public void register_a_route_without_cost() throws Throwable {
		RouteTestData sample = RouteTestData.sample();
		sample.cost = null;
		response = saveRoute(sample);
	}

	@When("^register a route without origin$")
	public void register_a_route_without_origin() throws Throwable {
		RouteTestData sample = RouteTestData.sample();
		sample.origin = null;
		response = saveRoute(sample);
	}

	@When("^register a route without destination$")
	public void register_a_route_without_destination() throws Throwable {
		RouteTestData sample = RouteTestData.sample();
		sample.destination = null;
		response = saveRoute(sample);
	}

	@When("^register a route time -(\\d+)$")
	public void register_a_route_time(double time) throws Throwable {
		RouteTestData sample = RouteTestData.sample();
		sample.time = -time;
		response = saveRoute(sample);
	}

	@When("^register a route cost -(\\d+)$")
	public void register_a_route_cost(double cost) throws Throwable {
		RouteTestData sample = RouteTestData.sample();
		sample.cost = -cost;
		response = saveRoute(sample);
	}

	@Given("^this registered route:$")
	public void this_registered_route(List<RouteTestData> route) throws Throwable {
		createdRouteId = Long.valueOf(saveRoute(route.get(0)).getBody());
	}

	@When("^edit this registered route with time (\\d+) and cost (\\d+)$")
	public void edit_this_registered_route_with_time_and_cost(double time, double cost) throws Throwable {
		RouteData route = getRouteDataFromCreatedRoute();
		route.setCost(cost);
		route.setTime(time);
		updateRoute(createdRouteId, route);
	}

	@Then("^the routes will be successfully edited$")
	public void the_routes_will_be_successfully_edited() throws Throwable {
	}

	@When("^edit this registered route without time$")
	public void edit_this_registered_route_without_time() throws Throwable {

		RouteData routeData = getRouteDataFromCreatedRoute();
		routeData.setTime(null);
		response = saveRoute(routeData);
	}

	@When("^edit this registered route without cost$")
	public void edit_this_registered_route_without_cost() throws Throwable {
		RouteData routeData = getRouteDataFromCreatedRoute();
		routeData.setCost(null);
		response = saveRoute(routeData);
	}

	@When("^edit this registered route without origin$")
	public void edit_this_registered_route_without_origin() throws Throwable {
		RouteData routeData = getRouteDataFromCreatedRoute();
		routeData.setOriginId(null);
		response = saveRoute(routeData);
	}

	@When("^edit this registered route without destination$")
	public void edit_this_registered_route_without_destination() throws Throwable {
		RouteData routeData = getRouteDataFromCreatedRoute();
		routeData.setDestinationId(null);
		response = saveRoute(routeData);
	}

	@When("^edit this route time -(\\d+)$")
	public void edit_this_route_time(double time) throws Throwable {
		RouteData routeData = getRouteDataFromCreatedRoute();
		routeData.setTime(-time);
		response = saveRoute(routeData);
	}

	@When("^edit this route cost -(\\d+)$")
	public void edit_this_route_cost(double cost) throws Throwable {
		RouteData routeData = getRouteDataFromCreatedRoute();
		routeData.setCost(-cost);
		response = saveRoute(routeData);
	}

	@When("^delete this registered route$")
	public void delete_this_registered_route() throws Throwable {
		response = testRestTemplate.exchange(RequestEntity.delete(URI.create("/routes/" + createdRouteId)).build(),
				Object.class);
	}

	@Then("^the routes will be successfully deleted$")
	public void the_routes_will_be_successfully_deleted() throws Throwable {
		requestOk();
	}

	private ResponseEntity<String> saveRoute(RouteTestData routeTestData) {
		RouteData routeData = RouteData.builder()
				.cost(routeTestData.cost)
				.time(routeTestData.time)
				.destinationId(getPointIdByName(routeTestData.destination))
				.originId(getPointIdByName(routeTestData.origin)).build();
		return saveRoute(routeData);
	}

	private Long getPointIdByName(String name) {
		return Optional.ofNullable(name).map(n-> getPointByName(n).getId()).orElse(null);
	}

	private ResponseEntity<String> saveRoute(RouteData routeData) {
		return testRestTemplate.postForEntity("/routes", routeData, String.class);
	}

	private ResponseEntity<String> updateRoute(Long id, RouteData routeData) {
		return testRestTemplate.exchange(RequestEntity.put(URI.create("/routes/"+id)).body(routeData), String.class);
	}
	
	
	private ResponseEntity<Route> getRouteById(Long id) {
		return testRestTemplate.getForEntity("/routes/" + id, Route.class);
	}

	private Point getPointByName(String name) {
		ResponseEntity<Point> point = testRestTemplate.getForEntity("/points/name/" + name, Point.class);
		return point.getBody();
	}

	private RouteData getRouteDataFromCreatedRoute() {
		Route route = getRouteById(createdRouteId).getBody();
		RouteData routeData = getRouteData(route);
		return routeData;
	}

	private RouteData getRouteData(Route route) {
		return RouteData.builder().cost(route.getCost()).time(route.getTime())
				.destinationId(route.getDestination().getId()).originId(route.getOrigin().getId()).build();
	}

	private void requestOk() {
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
	}

	private static class RouteTestData {

		private String origin;
		private String destination;
		private Double time;
		private Double cost;

		private static RouteTestData sample() {
			RouteTestData routeTestData = new RouteTestData();
			routeTestData.origin = "A";
			routeTestData.destination = "B";
			routeTestData.cost = 1d;
			routeTestData.time = 1d;
			return routeTestData;
		}
	}
}
