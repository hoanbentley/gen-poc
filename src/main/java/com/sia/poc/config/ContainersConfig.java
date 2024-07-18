package com.sia.poc.config;

//import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;

@Configuration
public class ContainersConfig {
    private static final Logger log = LoggerFactory.getLogger(ContainersConfig.class);

    /*@Bean
    @ServiceConnection
    MySQLContainer<?> sqlContainer () {
        return new MySQLContainer<>(DockerImageName.parse("mysql:8.0.33"));
    }*/

    /*@Bean
    public MySQLContainer<?> sqlContainer() {
        MySQLContainer<?> mysqlContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.33"));
        mysqlContainer.start();
        return mysqlContainer;
    }*/

    /*@DynamicPropertySource
    static void mysqlProperties(DynamicPropertyRegistry registry) {
        MySQLContainer<?> mysqlContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.2.0"));
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }*/

    @Bean
    public MySQLContainer<?> sqlContainer() {
        MySQLContainer<?> mysqlContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.33"))
                .withDatabaseName("gen-poc")
                .withUsername("dbuser")
                .withPassword("dbpassword")
                .withCommand("--default-authentication-plugin=mysql_native_password");
                //.withExposedPorts(3306);
                //.waitingFor(Wait.forListeningPort());
        try {
            mysqlContainer.start();
            log.info("Log success when starting mysql container");
        } catch (Exception ex) {
            log.error("Log error when starting mysql container: {}", ex.getMessage());
        }
        return mysqlContainer;
    }

    @Bean
    public DataSource dataSource(MySQLContainer<?> sqlContainer) {
        log.info("Datasource {}", sqlContainer.getJdbcUrl());
        HikariDataSource dataSource = new HikariDataSource();
        try {
            dataSource.setJdbcUrl(sqlContainer.getJdbcUrl());
            dataSource.setUsername(sqlContainer.getUsername());
            dataSource.setPassword(sqlContainer.getPassword());
            dataSource.setAutoCommit(false);
            log.info("Log success of datasource");
        } catch(Exception ex) {
            log.error("Error HikariDataSource {}", ex.getMessage());
        }
        return dataSource;
    }

}
