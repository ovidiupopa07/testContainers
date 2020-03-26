package com.example.testcontainers.mysql.repository;

import com.example.testcontainers.mysql.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Integer> {
}
