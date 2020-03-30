package com.example.testcontainers.dse.cassandra.repository;

import com.example.testcontainers.dse.cassandra.domain.Car;
import com.example.testcontainers.dse.cassandra.domain.CarPrimaryKey;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends ReactiveCassandraRepository<Car, CarPrimaryKey> {

}
