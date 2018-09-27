package org.danielmkraus.delivery.integration.steps;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.List;

import org.danielmkraus.delivery.domain.Point;
import org.danielmkraus.delivery.dto.PointData;
import org.danielmkraus.delivery.integration.SpringIntegrationTest;
import org.danielmkraus.delivery.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class PointSteps extends SpringIntegrationTest {
	
	@Autowired
	PointRepository repository;

	@Autowired
	TestRestTemplate template;
	
	PointData point;
	
	ResponseEntity<?> response;

	@Given("^these registered points:$")
	public void these_registered_points(List<Point> points) throws Throwable {
		points.forEach(repository::save);
	}

	@Given("^a new point with name \"([^\"]*)\"$")
	public void a_new_point_with_name(String name) throws Throwable {
		point = PointData.newInstance(name);
	}

	@When("^register this point$")
	public void register_this_point() throws Throwable {
		response = template.postForEntity("/points", point, Object.class);
	}

	@Then("^the point will be successfully registered$")
	public void the_point_will_be_successfully_registered() throws Throwable {
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
	}

	@Given("^a new point without name$")
	public void a_new_point_without_name() throws Throwable {
		point = PointData.newInstance(null);		
	}

	@Then("^will fail with message \"([^\"]*)\"$")
	public void will_fail_with_message(String message) throws Throwable {
		assertThat(response.getBody().toString()).contains(message);
	}

	@When("^delete the point with id (\\d+)$")
	public void delete_the_point_with_id(int id) throws Throwable {
		response = template.exchange("/points/" + id, HttpMethod.DELETE, null, Object.class);
	}

	@Given("^that no exists a point with id (\\d+)$")
	public void that_no_exists_a_point_with_id(long id) throws Throwable {
		assertThat(repository.findById(id)).isEmpty();
	}

	@Then("^will fail with status code (\\d+)$")
	public void will_fail_with_status_code(int statusCode) throws Throwable {
		assertThat(response.getStatusCodeValue()).isEqualByComparingTo(statusCode);
	}
	

	@When("^update point with name \"([^\"]*)\" to name \"([^\"]*)\"$")
	public void update_point_with_name_to_name(String name, String toName) throws Throwable {
		Point point = getPointByName(name).getBody();
		point.setName(toName);
		response = template.exchange(RequestEntity.put(URI.create("/points/" + point.getId())).body(PointData.newInstance(toName)), Object.class);
	}


	@Then("^the point will be successfully updated$")
	public void the_point_will_be_successfully_updated() throws Throwable {
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
	}

	@When("^find a point with id (\\d+)$")
	public void find_a_point_with_id(int id) throws Throwable {
		response = template.getForEntity("/points/" + id, Point.class);
	}

	@When("^delete the point with name \"([^\"]*)\"$")
	public void delete_the_point_with_name(String name) throws Throwable {
		response = template.exchange("/points/" + getPointByName(name).getBody().getId(), HttpMethod.DELETE, null, Object.class);
	}
	
	@Then("^the point \"([^\"]*)\" will be successfully deleted$")
	public void the_point_will_be_successfully_deleted(String name) throws Throwable {
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
		assertThat(getPointByName(name).getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
	}

	@When("^find a point with name (\\w+)$")
	public void find_a_point_with_name(String name) throws Throwable {
		response = template.getForEntity("/points/name/" + name, Point.class);
	}

	@Given("^that no exists a point with name \"([^\"]*)\"$")
	public void that_no_exists_a_point_with_name(String name) throws Throwable {
		assertThat(repository.findByName(name)).isNotPresent();
	}

	private ResponseEntity<Point> getPointByName(String name) {
		return template.getForEntity("/points/name/" + name, Point.class);
	}
}
