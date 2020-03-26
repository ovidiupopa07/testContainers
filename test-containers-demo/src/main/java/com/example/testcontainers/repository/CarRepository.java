package com.example.testcontainers.repository;

import com.example.testcontainers.domain.Car;
import com.example.testcontainers.domain.CarPrimaryKey;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends ReactiveCassandraRepository<Car, CarPrimaryKey>{

}
