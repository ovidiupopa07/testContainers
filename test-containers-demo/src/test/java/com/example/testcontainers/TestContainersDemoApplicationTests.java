package com.example.testcontainers;

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.testcontainers.domain.Car;
import com.example.testcontainers.repository.CarRepository;

@Testcontainers
@SpringBootTest
@ContextConfiguration(initializers = TestContainersDemoApplicationTests.Initializer.class)
@Profile("testcontainer")
class TestContainersDemoApplicationTests {

	@Container
	private static final CassandraContainer cassandraContainer = new CassandraContainer<>()
			.withInitScript("insert.cql");

	@Autowired
	CarRepository testClassRepository;

	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
			TestPropertyValues
					.of("spring.data.cassandra.contact-points=" + cassandraContainer.getContainerIpAddress(),
							"spring.data.cassandra.port=" + cassandraContainer.getMappedPort(9042),
							"spring.data.cassandra.keyspace-name=" + "test",
							"spring.data.cassandra.cluster-name=" + cassandraContainer.getCluster().getClusterName(),
							"spring.data.cassandra.username=" + cassandraContainer.getUsername(),
							"spring.data.cassandra.password=" + cassandraContainer.getPassword())
					.applyTo(configurableApplicationContext.getEnvironment());
		}
	}

	@Test
	void contextLoads() {
		testClassRepository.findAll().doOnNext(new Consumer<Car>() {

			@Override
			public void accept(Car t) {
				System.out.println(t.getCarPrimaryKey().getBrand()+ " " +t.getModel());
			}
		}).subscribe();
	}

}
