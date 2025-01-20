package com.example.testtask;

import com.example.testtask.config.DatabaseProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@EnableConfigurationProperties(DatabaseProperties.class)
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TestTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestTaskApplication.class, args);
    }

    @Bean
    public Map<String, JdbcTemplate> jdbcTemplates(DatabaseProperties properties) {
        Map<String, JdbcTemplate> templates = new HashMap<>();
        for (DatabaseProperties.DataSourceConfig config : properties.getDataSources()) {
            DataSource dataSource = createDataSource(config);
            templates.put(config.getName(), new JdbcTemplate(dataSource));
        }
        return templates;
    }

    private DataSource createDataSource(DatabaseProperties.DataSourceConfig config) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(config.getUrl());
        dataSource.setUsername(config.getUser());
        dataSource.setPassword(config.getPassword());
        return dataSource;

    }
}
