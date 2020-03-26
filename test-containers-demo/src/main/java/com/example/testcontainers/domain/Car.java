package com.example.testcontainers.domain;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("car")
public class Car {
	
	@PrimaryKey
	private CarPrimaryKey carPrimaryKey;
	
	@Column("model")
	private String model;
	
	@Column("horse_power")
	private Integer horsePower;
	
	@Column("fuel")
	private String fuel;
	
	@Column("torque")
	private String torque;

}
