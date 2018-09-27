package org.danielmkraus.delivery.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder 
@NoArgsConstructor
@AllArgsConstructor
public class RouteData {

	@NotNull(message = "{Route.origin.null}")
	private Long originId;
	
	@NotNull(message = "{Route.destination.null}")
	private Long destinationId;
	
	@NotNull(message = "{Route.time.null}")
	@DecimalMin(value="0", message = "{Route.time.negative}")
	private Double time;

	@NotNull(message = "{Route.cost.null}")
	@DecimalMin(value="0", message = "{Route.cost.negative}")
	private Double cost;
	
}

