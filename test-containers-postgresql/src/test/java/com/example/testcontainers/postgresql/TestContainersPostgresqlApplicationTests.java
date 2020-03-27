package com.example.testcontainers.postgresql;

import com.example.testcontainers.postgresql.domain.Car;
import com.example.testcontainers.postgresql.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@ContextConfiguration(initializers = TestContainersPostgresqlApplicationTests.Initializer.class)
@SpringBootTest
class TestContainersPostgresqlApplicationTests {

    @Container
    static PostgreSQLContainer postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("integration-test-db")
            .withUsername("sa")
            .withPassword("sa")
            .withInitScript("init.sql");

    @Autowired
    private CarRepository carRepository;


    @Test
    void getCarsFromPostgreSql() {


        final Flux<Car> allCars = carRepository.findAll();
        StepVerifier.create(allCars).assertNext(nextCar -> {
            assertEquals("VW", nextCar.getBrand());
            assertEquals("Tiguan", nextCar.getModel());
            assertEquals(200, nextCar.getHorsePower());
        }).verifyComplete();
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

            TestPropertyValues
                    .of(
                            "spring.datasource.host=localhost",
                            "spring.datasource.port=" + postgreSQLContainer.getFirstMappedPort(),
                            "spring.datasource.database=" + postgreSQLContainer.getDatabaseName(),
                            "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                            "spring.datasource.password=" + postgreSQLContainer.getPassword())
                    .applyTo(configurableApplicationContext.getEnvironment());
        }
    }

}
