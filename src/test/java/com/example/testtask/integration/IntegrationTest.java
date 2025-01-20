package com.example.testtask.integration;

import com.example.testtask.dto.UserDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES1 = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("db1")
            .withUsername("user1")
            .withPassword("pass1");

    @Container
    private static final PostgreSQLContainer<?> POSTGRES2 = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("db2")
            .withUsername("user2")
            .withPassword("pass2");

    @DynamicPropertySource
    static void registerPgProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        registry.add("database-properties.datasources[0].url", POSTGRES1::getJdbcUrl);
        registry.add("database-properties.datasources[0].user", POSTGRES1::getUsername);
        registry.add("database-properties.datasources[0].password", POSTGRES1::getPassword);
        registry.add("database-properties.datasources[0].name", () -> "data-base-1");
        registry.add("database-properties.datasources[0].table",  () -> "users");
        registry.add("database-properties.datasources[0].mapping.id",  () -> "user_id");
        registry.add("database-properties.datasources[0].mapping.username",  () -> "login");
        registry.add("database-properties.datasources[0].mapping.name",  () -> "first_name");
        registry.add("database-properties.datasources[0].mapping.surname",  () -> "last_name");

        registry.add("database-properties.datasources[1].url", POSTGRES2::getJdbcUrl);
        registry.add("database-properties.datasources[1].user", POSTGRES2::getUsername);
        registry.add("database-properties.datasources[1].password", POSTGRES2::getPassword);
        registry.add("database-properties.datasources[1].name", () -> "data-base-2");
        registry.add("database-properties.datasources[1].table",  () -> "user_table");
        registry.add("database-properties.datasources[1].mapping.id",  () -> "ldap_login");
        registry.add("database-properties.datasources[1].mapping.username",  () -> "ldap_login");
        registry.add("database-properties.datasources[1].mapping.name",  () -> "name");
        registry.add("database-properties.datasources[1].mapping.surname",  () -> "surname");
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    static void setupDatabases() {
        // Initialize test data in POSTGRES1
        try (var connection = POSTGRES1.createConnection("")) {
            connection.createStatement().executeUpdate(
                    "CREATE TABLE users (user_id VARCHAR(255), login VARCHAR(255), first_name VARCHAR(255), last_name VARCHAR(255));");
            connection.createStatement().executeUpdate(
                    "INSERT INTO users (user_id, login, first_name, last_name) VALUES ('1','user1', 'John', 'Doe');");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database 1", e);
        }

        // Initialize test data in POSTGRES2
        try (var connection = POSTGRES2.createConnection("")) {
            connection.createStatement().executeUpdate(
                    "CREATE TABLE user_table (ldap_login VARCHAR(255), name VARCHAR(255), surname VARCHAR(255));");
            connection.createStatement().executeUpdate(
                    "INSERT INTO user_table (ldap_login, name, surname) VALUES ('login','Jane', 'Doe');");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database 2", e);
        }
    }

    @Test
    public void testGetUsers() {
        System.out.println(POSTGRES1.getLogs());
        System.out.println(POSTGRES2.getLogs());
        ResponseEntity<UserDTO[]> response = restTemplate.getForEntity("/users", UserDTO[].class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        List<UserDTO> users = List.of(response.getBody());
        assertThat(users).hasSize(2);

        assertThat(users).anyMatch(user -> "John".equals(user.getName()) && "Doe".equals(user.getSurname()));
        assertThat(users).anyMatch(user -> "Jane".equals(user.getName()) && "Doe".equals(user.getSurname()));
    }
}
