package com.example.testcontainers.dseserver.repository;

import com.example.testcontainers.dseserver.domain.Car;
import com.example.testcontainers.dseserver.domain.CarPrimaryKey;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CarRepository extends ReactiveCassandraRepository<Car, CarPrimaryKey> {


    @Query
    Flux<Car> findBySolrQuery(String solrQuery);
}
