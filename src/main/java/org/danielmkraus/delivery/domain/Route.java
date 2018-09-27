package org.danielmkraus.delivery.domain;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.danielmkraus.delivery.validation.Update;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

@RelationshipEntity(type = "GO_TO")

@Valid
public class Route {

	@Id @GeneratedValue
	@Getter
	@NotNull(message = "{Route.id.null}", groups = Update.class)
	private Long id;

	@StartNode
	@Getter
	@NotNull(message = "{Route.origin.null}")
	private Point origin;

	@EndNode
	@Getter
	@NotNull(message = "{Route.destination.null}")
	private Point destination;

	@Property
	@Getter
	@NotNull(message = "{Route.time.null}") @DecimalMin(value = "0", message = "{Route.time.negative}")
	private Double time;

	@Property
	@Getter
	@NotNull(message = "{Route.cost.null}") @DecimalMin(value = "0", message = "{Route.cost.negative}")
	private Double cost;
}
