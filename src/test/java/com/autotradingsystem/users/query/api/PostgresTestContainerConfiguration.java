package com.autotradingsystem.users.query.api;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
@TestConfiguration(proxyBeanMethods = false)
public class PostgresTestContainerConfiguration {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public PostgreSQLContainer<?> postgresContainer(){
        PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:13.3")
                .withDatabaseName("testdb")
                .withUsername("user")
                .withPassword("password");
        return container;
    }

    @Bean
    DataSource dataSource(PostgreSQLContainer<?> postgreSQLContainer){
        var hikari = new HikariDataSource();
        hikari.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
        hikari.setUsername(postgreSQLContainer.getUsername());
        hikari.setPassword(postgreSQLContainer.getPassword());
        return hikari;
    }
}
