package org.danielmkraus.delivery.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor(staticName="newInstance")
public class PointData {
	
	@NotEmpty(message="{Point.name.empty}")
	private String name;
}
