package com.example.testcontainers.postgresql.repository;

import com.example.testcontainers.postgresql.domain.Car;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends ReactiveCrudRepository<Car, Integer> {
}
