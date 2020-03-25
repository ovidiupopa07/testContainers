package com.example.testcontainers;

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.testcontainers.domain.TestClass;
import com.example.testcontainers.repository.TestClassRepository;

@Testcontainers
@SpringBootTest
@ContextConfiguration(initializers = TestContainersDemoApplicationTests.Initializer.class)
@Profile("testcontainer")
class TestContainersDemoApplicationTests {
	
	@Container
	private static final CassandraContainer cassandraContainer = new CassandraContainer<>()
	.withInitScript("insert.cql");
	
	@Autowired
	TestClassRepository testClassRepository;
	

	
	static class Initializer
    implements ApplicationContextInitializer<ConfigurableApplicationContext> {
      public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
          TestPropertyValues.of(
            "spring.data.cassandra.contact-points=" + cassandraContainer.getContainerIpAddress(),
            "spring.data.cassandra.port=" + cassandraContainer.getMappedPort(9042),
            "spring.data.cassandra.keyspace-name=" + "test",
            "spring.data.cassandra.cluster-name=" + cassandraContainer.getCluster().getClusterName(),
            "spring.data.cassandra.username=" + cassandraContainer.getUsername(),
            "spring.data.cassandra.password=" + cassandraContainer.getPassword()
          ).applyTo(configurableApplicationContext.getEnvironment());
      }
  }
	
	
	
	@Test
	void contextLoads() {
		testClassRepository.findAll().doOnNext(new Consumer<TestClass>() {

			@Override
			public void accept(TestClass t) {
				System.out.println(t.getLastName());
			}
		}).subscribe();
	}

}
