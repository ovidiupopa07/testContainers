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
@Table("test_table")
public class TestClass {
	
	@PrimaryKey
	private TestClassPrimaryKey testClassPrimaryKey;
	
	@Column("last_name")
	private String lastName;

}
