package com.example.testcontainers.postgresql.configuration;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories(basePackages = "com.example.testcontainers.postgresql.repository")
public class PostgresConfiguration extends AbstractR2dbcConfiguration {

    @Value("${spring.datasource.port}")
    private int postgressport;

    @Value("${spring.datasource.host}")
    private String postgreshost;

    @Value("${spring.datasource.username}")
    private String postgresusername;

    @Value("${spring.datasource.password}")
    private String postgrespwd;

    @Value("${spring.datasource.database}")
    private String postgresdb;


    @Bean
    @Override
    public ConnectionFactory connectionFactory() {
        final PostgresqlConnectionConfiguration build = PostgresqlConnectionConfiguration.builder()
                .host(postgreshost)
                .port(postgressport)
                .username(postgresusername)
                .password(postgrespwd)
                .database(postgresdb)
                .build();
        final PostgresqlConnectionFactory postgresqlConnectionFactory = new PostgresqlConnectionFactory(build
        );
        return postgresqlConnectionFactory;
    }

}
