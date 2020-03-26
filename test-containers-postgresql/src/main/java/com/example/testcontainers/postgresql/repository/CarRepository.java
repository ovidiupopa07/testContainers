package com.example.testcontainers.postgresql.repository;

import com.example.testcontainers.postgresql.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Integer> {
}
