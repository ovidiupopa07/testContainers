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
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Testcontainers
@ContextConfiguration(initializers = TestContainersPostgresqlApplicationTests.Initializer.class)
@SpringBootTest
class TestContainersPostgresqlApplicationTests {

    @Container
    static PostgreSQLContainer postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("integration-test-db")
            .withUsername("sa")
            .withPassword("sa");

    @Autowired
    private CarRepository carRepository;


    @Test
    @Sql({"/init.sql"})
    void getCarsFromPostgreSql() {


        final List<Car> allCars = carRepository.findAll();
        assertFalse(allCars.isEmpty());
        assertEquals(1, allCars.size());

        final Car car = allCars.get(0);

        assertEquals("VW", car.getBrand());
        assertEquals("Tiguan", car.getModel());
        assertEquals(200, car.getHorsePower());
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues
                    .of(
                            "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                            "spring.datasource.driver-class-name=" + postgreSQLContainer.getDriverClassName())
                    .applyTo(configurableApplicationContext.getEnvironment());
        }
    }

}
