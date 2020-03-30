package com.example.testcontainers.dseserver;

import com.example.testcontainers.dseserver.domain.Car;
import com.example.testcontainers.dseserver.dse.DseServerContainer;
import com.example.testcontainers.dseserver.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@ContextConfiguration(initializers = TestContainersDseServerApplicationTests.Initializer.class)
@SpringBootTest
class TestContainersDseServerApplicationTests {

    @Container
    private static final DseServerContainer dseCassandraContainer = new DseServerContainer<>("5.1.10")
            .withInitScript("insert.cql").withStartupTimeout(Duration.ofMillis(500000));

    @Autowired
    CarRepository testClassRepository;

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues
                    .of("spring.data.cassandra.contact-points=" + dseCassandraContainer.getContainerIpAddress(),
                            "spring.data.cassandra.port=" + dseCassandraContainer.getMappedPort(9042),
                            "spring.data.cassandra.keyspace-name=" + "test",
                            "spring.data.cassandra.cluster-name=" + dseCassandraContainer.getCluster().getClusterName(),
                            "spring.data.cassandra.username=" + dseCassandraContainer.getUsername(),
                            "spring.data.cassandra.password=" + dseCassandraContainer.getPassword())
                    .applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Test
    void getNextCar() {
        final Flux<Car> carFlux = testClassRepository.findBySolrQuery("'{\"q\":\"*:*\", \"fq\":\"model:Tiguan\"}'");


        StepVerifier.create(carFlux).assertNext(car -> {
            assertEquals("VW", car.getCarPrimaryKey().getBrand());
            assertEquals("Tiguan", car.getModel());
            assertEquals(200, car.getHorsePower());
        }).verifyComplete();
    }
}
