package com.example.testcontainers.mysql;

import com.example.testcontainers.mysql.domain.Car;
import com.example.testcontainers.mysql.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Testcontainers
@ContextConfiguration(initializers = TestContainersMysqlApplicationTests.Initializer.class)
@SpringBootTest
class TestContainersMysqlApplicationTests {

    @Container
    private static final MySQLContainer mySQLContainer = (MySQLContainer) new MySQLContainer("mysql:latest")
            .withDatabaseName("car").withUsername("sa").withPassword("sa").withInitScript("init.sql");


    @Autowired
    private CarRepository carRepository;

    @Test
    void getCarFromMySqlTestContainer() {

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
                            "spring.datasource.url=" + mySQLContainer.getJdbcUrl(),
                            "spring.datasource.driver-class-name=" + mySQLContainer.getDriverClassName())
                    .applyTo(configurableApplicationContext.getEnvironment());
        }
    }


}
