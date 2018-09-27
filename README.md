# Routing exercise

[ ![Codeship Status for danielmkraus/routing-exercise](https://app.codeship.com/projects/7f162ba0-a340-0136-d1b5-66bb310eeefb/status?branch=master)](https://app.codeship.com/projects/307338) 

## Indroduction

This is a sample microservice to:

 * manage points - representing a location.
 * manage routes - a link between two points, that have a *cost* and *time* associated to generate fastest and cheapest paths.
 * retrieve paths - a sequence of points based on routes, can find cheapest and fastest paths and specify a cost for time to 
 	calculate cheapest path.

### Assuptions

To develop this service, we have these assumptions:
 
 * Every point needs to have a unique name.
 * Every route needs to have a origin, destination, cost and time associated.
 * Cost and time of points can be zero, but not negative.
 * Can have more than one route with same origin and destination.

For more behavior details, you can check out behavior test scenarios at [Point feature](./src/test/resources/features/Point.feature) and [Route feature](./src/test/resources/features/Route.feature) and setup program and start the program to see swagger documentation at /swagger-ui.html URI.

### Notes

 * The system will load data from CSV located at [./src/main/resources/routes.csv](./src/main/resources/routes.csv) to database on startup. 	

### Tecnology

This project is using these following technologies/frameworks:

Base:

* [Spring boot](https://spring.io/projects/spring-boot) framework.
* [Undertow](http://undertow.io/) for web server.

Validation: 

* [Bean validation](https://beanvalidation.org/) for data validation.

Api documentation:

* [Swagger](https://swagger.io/) for rest api documentation.

Testing:

* [JUnit](https://junit.org/junit4/) as test base execution framework.
* [Spring test](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html) for test with spring framework.
* [Assertj](http://joel-costigliola.github.io/assertj/) for assertion framework.
* [Mockito](https://site.mockito.org/) for mocking.
* [Cucumber](https://cucumber.io/) for BDD scenarios.

Logging:

* [Slf4j](https://www.slf4j.org/) as logging interface.
* [Logback](https://logback.qos.ch/) as log provider for slf4j interface.

Code generation:

* [Lombok](https://projectlombok.org/) as a tool to generate automatically getters, setters, equals, hashcode, constructors and builders 
	(boilerplate code) by annotations, need to configure the IDE to work properly.

Database:

* [Neo4j](https://neo4j.com/) as a graph database.

### Running

#### Requirements 

* Java 1.8 
* Maven
* Docker (if you want to run on docker container engine)

#### Setting up

To run this app you can run by docker, maven or java executable running following commands at root folder of project:

maven: ``mvn spring-boot:run``

java:

* building jar: ``mvn clean -U package``
* running jar: ``java -jar ./target/delivery-exercise-1.0.0.jar``


docker:

* building image: ``mvn clean -U package docker:build``
* running: docker: ``docker run -e SPRING_DATA_NEO4J_URI=bolt://172.17.0.1 -e SPRING_DATA_NEO4J_USERNAME=user -e SPRING_DATA_NEO4J_PASSWORD=user  --name delivery-exercise delivery-exercise:1.0.0`` 

docker-compose:

I configured a docker compose with neo4j and delivery-exercise containers at [docker-compose.yml](./docker-compose.yml)

* running compose with database and system: ``docker-compose up -d``

### Roadmap

 * Define a strategy to store and preprocess routes.
 * Authentication with oauth
