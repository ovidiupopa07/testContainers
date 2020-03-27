package com.example.testcontainers.dse.cassandra.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("car")
public class Car {

    @PrimaryKey
    private CarPrimaryKey carPrimaryKey;

    @Column("model")
    private String model;

    @Column("horse_power")
    private Integer horsePower;

    @Column("fuel")
    private String fuel;

    @Column("torque")
    private String torque;

}
