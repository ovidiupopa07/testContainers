package com.example.testcontainers.postgresql.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("car")
@Data
public class Car {

    @Id
    private Integer id;

    @Column
    private String brand;

    @Column
    private String model;

    @Column
    private Integer horsePower;

    @Column
    private String fuel;

    @Column
    private String torque;

}
