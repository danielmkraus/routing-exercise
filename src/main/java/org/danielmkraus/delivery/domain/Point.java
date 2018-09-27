package org.danielmkraus.delivery.domain;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.danielmkraus.delivery.validation.Update;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NodeEntity
@Data
@EqualsAndHashCode(of="id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Point {

	@Id	@GeneratedValue 
	@NotNull(message="{Point.id.null}", groups=Update.class)
	private Long id; 
	
	@Index(unique = true)
	@NotEmpty(message="{Point.name.empty}")
	private String name;
	
}


