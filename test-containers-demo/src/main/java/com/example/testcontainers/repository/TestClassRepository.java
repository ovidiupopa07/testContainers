package com.example.testcontainers.repository;

import com.example.testcontainers.domain.TestClass;
import com.example.testcontainers.domain.TestClassPrimaryKey;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestClassRepository extends ReactiveCassandraRepository<TestClass, TestClassPrimaryKey>{

}
