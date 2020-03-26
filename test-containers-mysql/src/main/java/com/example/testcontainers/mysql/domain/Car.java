package com.example.testcontainers.mysql.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
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
