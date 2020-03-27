package com.example.testcontainers.dse.cassandra;

import com.example.testcontainers.dse.cassandra.domain.Car;
import com.example.testcontainers.dse.cassandra.dse.DseCassandraContainer;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@ContextConfiguration(initializers = TestContainersDseCassandraApplicationTests.Initializer.class)
@SpringBootTest
class TestContainersDseCassandraApplicationTests {

    @Container
    private static final DseCassandraContainer dseCassandraContainer = new DseCassandraContainer<>("4.0")
            .withInitScript("insert.cql");

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
        final Flux<Car> carFlux = testClassRepository.findAll();


        StepVerifier.create(carFlux).assertNext(car -> {
            assertEquals("VW", car.getCarPrimaryKey().getBrand());
            assertEquals("Tiguan", car.getModel());
            assertEquals(200, car.getHorsePower());
        }).verifyComplete();
    }
}
