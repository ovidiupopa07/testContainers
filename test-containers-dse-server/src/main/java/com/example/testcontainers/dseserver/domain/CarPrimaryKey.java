package com.example.testcontainers.dseserver.domain;

import com.datastax.driver.core.utils.UUIDs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyClass
public class CarPrimaryKey {

    @PrimaryKeyColumn(name = "brand", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String brand;
    @PrimaryKeyColumn(name = "uuid", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private UUID uuid = UUIDs.timeBased();
}
